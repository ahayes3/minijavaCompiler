import org.objectweb.asm.{ClassWriter, Label, MethodVisitor, Opcodes}

import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class Local(varNum: Int, value: Any)

case class Counter() {
  var c = 0

  def add(): Unit = {
    c += 1
  }

  def get(): Int = {
    c
  }

  def next(): Int = {
    val a = c
    c += 1
    a
  }
}

class CodeGenerationError extends Exception{}
//NOTES
//Each lambda instance should generate a .class and put the bytes in lambdaClasses. The name will follow the name of the name of the class it is found in with $(number) appended

object GenerateCode {
  val typeMap: Map[Type, String] = HashMap(IntType -> "I", BoolType -> "Z", ArrType(IntType) -> "[I", ArrType(StringType) -> "[java/lang/String")
  var hier: Hierarchy = _

  val lambdaClasses: mutable.ArrayBuffer[(Array[Byte], String)] = new ArrayBuffer[(Array[Byte], String)]()
  val lambdaCounts = new mutable.HashMap[String, Int]
  val lambdaWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES)

  def apply(goal: Goal, ctx: Hierarchy): Seq[(Array[Byte], String)] = {
    hier = ctx
    val cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES)
    visitMain(goal.main, cw)

    goal.classes.foreach(p => lambdaCounts.put(p.ident, 1)) //counting for naming of lambda file
    lambdaCounts.put(goal.main.ident, 1)

    val classes = goal.classes.map(p => (visitClass(p, cw, ctx.findClass(p.ident).get), p.ident))
    val lambdas = goal.lambdas.map(p => (visitLambda(p, cw), p.ident))

    classes :++ lambdas :++ lambdaClasses
  }

  def visitMain(clazz: MainClass, cw: ClassWriter): Array[Byte] = {
    cw.visit(Opcodes.V13, Opcodes.ACC_PUBLIC, clazz.ident, null, "java/main/Object", null)
    var mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
    mv.visitCode()
    mv.visitVarInsn(Opcodes.ALOAD, 0)
    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
    mv.visitInsn(Opcodes.RETURN)
    mv.visitMaxs(-1, -1)
    mv.visitEnd()

    mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String)V", null, null)
    visitPrint(clazz.statement.asInstanceOf[PrintStatement], mv, hier.main.findMethod("main").get, new mutable.HashMap())
    mv.visitMaxs(-1, -1)
    mv.visitEnd()

    cw.visitEnd()
    cw.toByteArray
  }

  def visitClass(clazz: Clazz, cw: ClassWriter, ctx: CNode): Array[Byte] = {
    cw.visit(Opcodes.V13, Opcodes.ACC_PUBLIC, clazz.ident, null, clazz.parent, null)
    clazz.varDecs.foreach(visitField(_, cw, ctx))
    clazz.methods.foreach(p => visitMethod(p, cw, ctx.findMethod(p.ident).get))
    cw.visitEnd()
    cw.toByteArray
  }

  def visitLambda(i: LambdaI, cw: ClassWriter): Array[Byte] = {
    cw.visit(Opcodes.V13, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, i.ident, null, "java/lang/Object", null)
    var mv = cw.visitMethod(0, "", "()V", null, null) //"abstract constructor"
    mv.visitCode()
    mv.visitVarInsn(Opcodes.ALOAD, 0)
    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "", "()V", false)
    mv.visitInsn(Opcodes.RETURN)
    mv.visitMaxs(-1, -1)
    mv.visitEnd()

    mv = cw.visitMethod(Opcodes.ACC_PUBLIC, i.methodIdent, getDescriptor(i.params.types, i.mTipe), "()V", null)
    mv.visitEnd()
    cw.visitEnd()
    cw.toByteArray
  }

  def visitField(field: VarDeclaration, cw: ClassWriter, ctx: CNode): Unit = {
    cw.visitField(Opcodes.ACC_PUBLIC, field.ident, getDescriptor(field.tipe), null, null).visitEnd()
  }

  def visitMethod(meth: MethodDec, cw: ClassWriter, ctx: MNode): Unit = {
    val mv = cw.visitMethod(Opcodes.ACC_PUBLIC, meth.ident, getDescriptor(meth.params.types, meth.tipe), null, null)
    mv.visitVarInsn(Opcodes.ALOAD, 0) //loads "this"
    mv.visitCode()
    visitCode(meth.statements, mv, ctx, new mutable.HashMap[(String, Int), Local](), Counter())
    mv.visitMaxs(-1, -1) //args should be ignored and computed automatically
    mv.visitEnd()
  }

  def visitCode(statements: Seq[Statement], mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local], ctr: Counter): Unit = {
    for (i <- statements) {
      i match {
        case e: BlockStatement => visitCode(e.value, mv, ctx, versions, ctr)
        case e: VarDeclaration => visitVarDec(e, mv, ctx)
        case e: Assignment => visitAssign(e, mv, ctx, versions, ctr)
        case e: ArrAssign => visitArrAssign(e, mv, ctx, versions, ctr)
        case e: PrintStatement => visitPrint(e, mv, ctx, versions)
        case e: WhileStatement => visitWhile(e, mv, ctx, versions, ctr)
        case e: IfStatement => visitIf(e, mv, ctx, versions, ctr)
      }
    }
  }

  def visitVarDec(v: VarDeclaration, mv: MethodVisitor, ctx: MNode): Unit = {
    //locals.put("ABc",locals.size) //todo might need work
    var ml = 0
    v.tipe match {
      case IntType =>
        ctx.locals.put(v.ident, (IntType, 0))
      case BoolType =>
        ctx.locals.put(v.ident, (BoolType, 0))
      case e: IdentType =>
        ctx.locals.put(v.ident, (e, 0))
      case StringType =>
        ctx.locals.put(v.ident, (StringType, 0))
      case e: ArrType =>
        ctx.locals.put(v.ident, (e, 0))
    }
  }

  def visitAssign(a: Assignment, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local], ctr: Counter): Unit = {
    val tipe = ctx.fields(a.ident)
    if (hier.containsLambda(tipe.toString))
      visitLambdaBlock(a.value.asInstanceOf[LambdaBlock], mv, ctx, versions, tipe.asInstanceOf[IdentType])
    else
      visitExp(a.value, mv, ctx, versions)
    //val version = ctr.next()
    tipe match {
      case IntType | BoolType => mv.visitInsn(Opcodes.ISTORE)
      case _: IdentType => mv.visitInsn(Opcodes.ASTORE)
    }
    val version = ctx.locals(a.ident)._2 + 1
    ctx.locals.remove(a.ident)
    //val old = versions.keys.find(p => p._1 == a.ident && p._2 == version).get
    versions.put((a.ident, version), Local(ctr.next(), a.value))
    ctx.locals.put(a.ident, (tipe,version))

  }

  def visitArrAssign(a: ArrAssign, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local], counter: Counter): Unit = {
    visitExp(a.offset, mv, ctx, versions)
    visitExp(a.value, mv, ctx, versions)
    mv.visitInsn(Opcodes.IASTORE)
  }

  def visitPrint(statement: PrintStatement, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local]): Unit = {
    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
    visitExp(statement.value, mv, ctx, versions)
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false)
  }

  def visitWhile(statement: WhileStatement, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local], ctr: Counter): Unit = {
    val l1, l3 = new Label
    mv.visitLabel(l1)
    visitExp(statement.condition, mv, ctx, versions)
    mv.visitJumpInsn(Opcodes.IFEQ, l3)
    visitCode(Array[Statement](statement.statement), mv, ctx, versions, ctr)
    mv.visitJumpInsn(Opcodes.GOTO, l1)

    mv.visitLabel(l3)
  }

  def visitIf(statement: IfStatement, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local], ctr: Counter): Unit = {
    val l1, l2 = new Label
    visitExp(statement.condition, mv, ctx, versions)
    mv.visitJumpInsn(Opcodes.IFEQ, l1)
    visitCode(Array[Statement](statement.statement), mv, ctx, versions, ctr)
    mv.visitJumpInsn(Opcodes.GOTO, l2)
    mv.visitLabel(l1)
    visitCode(Array[Statement](statement.elseStatement), mv, ctx, versions, ctr)
    mv.visitLabel(l2)
  }

  //Should evaluate to bytecode and leave result on top of stack
  def visitExp(exp: Expression, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local]): Unit = {
    exp match {
      case e: SumExpression =>
        visitExp(e.left, mv, ctx, versions)
        visitExp(e.right, mv, ctx, versions)
        mv.visitInsn(Opcodes.IADD)
      case e: SubExpression =>
        visitExp(e.left, mv, ctx, versions)
        visitExp(e.right, mv, ctx, versions)
        mv.visitInsn(Opcodes.ISUB)
      case e: MulExpression =>
        visitExp(e.left, mv, ctx, versions)
        visitExp(e.right, mv, ctx, versions)
        mv.visitInsn(Opcodes.IMUL)
      case e: LessExpression => //loads 0 or 1 onto stack
        visitExp(e.left, mv, ctx, versions)
        visitExp(e.right, mv, ctx, versions)
        val l1 = new Label
        val l2 = new Label
        mv.visitJumpInsn(Opcodes.IF_ICMPLT, l1)
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitJumpInsn(Opcodes.GOTO, l2)
        mv.visitLabel(l1)
        mv.visitInsn(Opcodes.ICONST_1)
        mv.visitLabel(l2)
      case e: AndExpression =>
        visitExp(e.left, mv, ctx, versions)
        visitExp(e.right, mv, ctx, versions)
        mv.visitInsn(Opcodes.IAND)
      case e: Ident =>
        val ver = ctx.locals(e.ident)
        val v = versions((e.ident,ver._2))
        ver._1 match {
          case IntType =>
            mv.visitVarInsn(Opcodes.ILOAD,v.varNum)
          case BoolType =>
            mv.visitVarInsn(Opcodes.ILOAD,v.varNum)
          case _:IdentType => mv.visitVarInsn(Opcodes.ALOAD,v.varNum)
          case _ => throw new CodeGenerationError
        }
      case e: IntLit =>
        e.value match {
          case 0 | 1 | 2 | 3 | 4 | 5 => mv.visitInsn(Opcodes.ICONST_0 + e.value)
          case _ if e.value < 128 & e.value >= -128 => mv.visitIntInsn(Opcodes.BIPUSH, _)
          case _ if e.value >= Short.MinValue && e.value <= Short.MaxValue => mv.visitIntInsn(Opcodes.SIPUSH, _)
          case _ => mv.visitLdcInsn(_)
        }
      case e: BoolLit =>
        if (e.value)
          mv.visitInsn(Opcodes.ICONST_1)
        else
          mv.visitInsn(Opcodes.ICONST_0)
      case e: ArrAccess =>
        visitExp(e.array, mv, ctx, versions)
        visitExp(e.offset, mv, ctx, versions)
        mv.visitInsn(Opcodes.IALOAD)
      case e: DotExpression =>
        visitExp(e.left,mv,ctx,versions)

      case e: LengthExpression =>
        visitExp(e.expression, mv, ctx, versions)
        mv.visitInsn(Opcodes.ARRAYLENGTH)
      case e: ThisExpression =>
        mv.visitVarInsn(Opcodes.ALOAD, 0)
      case e: NewIntArrExpression =>
        visitExp(e.size, mv, ctx, versions)
        mv.visitInsn(Opcodes.NEWARRAY)
      case e: NewIdentExpression =>
        mv.visitTypeInsn(Opcodes.NEW,e.ident)
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
          hier.findClass(e.ident).get.clazz.parent,
          "<init>",
          "()V",
          false)
      case e: NegateExpression =>
        visitExp(e.expression, mv, ctx, versions)
        mv.visitInsn(Opcodes.ICONST_1)
        mv.visitInsn(Opcodes.IXOR)
      case e: ParenExpression =>
        visitExp(e.expression, mv, ctx, versions)
      case e: ReturnExpression =>
        visitExp(e.expression, mv, ctx, versions)
        e.tipe match {
          case IntType | BoolType => mv.visitInsn(Opcodes.IRETURN)
          case _: IdentType | _: ArrType => mv.visitInsn(Opcodes.ARETURN)
        }

    }
  }

  def loadVar(mv:MethodVisitor,tipe:Type, varNum:Int): Unit = {
    tipe match {
      case IntType|BoolType => mv.visitVarInsn(Opcodes.ILOAD,varNum)
      case _:IdentType => mv.visitVarInsn(Opcodes.ALOAD,varNum)
      case _:ArrType => mv.visitVarInsn(Opcodes.ALOAD,varNum)
    }
  }

  def visitLambdaBlock(l: LambdaBlock, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local], tipe: IdentType): Unit = {
    val thisClass = ctx.getThis.ident
    val num = lambdaCounts.get(thisClass)
    lambdaWriter.visit(Opcodes.V13, Opcodes.ACC_PUBLIC, thisClass + "$" + num.get, null, tipe.ident, null)
  }

  def getDescriptor(t: Type): String = {
    t match {
      case IntType => "I"
      case VoidType => "V"
      case BoolType => "Z"
      case StringType => "Ljava/lang/String"
      case e: ArrType => "[" + getDescriptor(e.tipe)
      case e: IdentType => "L" + e.ident
    }
  }

  def getDescriptor(params: Seq[Type], ret: Type): String = {
    var out = "("
    params.foreach(out += getDescriptor(_))
    out += ")"
    out += getDescriptor(ret)
    out
  }
}
