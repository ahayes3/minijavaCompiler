import org.objectweb.asm.util.CheckClassAdapter
import org.objectweb.asm.{ClassWriter, Label, MethodVisitor, Opcodes}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class Local(varNum: Int, value: Any)

class Counter() {
  var c = 0

  def add(): Unit = {
    c += 1
  }

  def get(): Int = {
    c
  }

  def next(): Int = {
    add()
    get()
  }
}

class CodeGenerationError extends Exception {}

//NOTES
//Each lambda instance should generate a .class and put the bytes in lambdaClasses. The name will follow the name of the name of the class it is found in with $(number) appended

object GenerateCode {
  //val typeMap: Map[Type, String] = HashMap(IntType -> "I", BoolType -> "Z", ArrType(IntType) -> "[I", ArrType(StringType) -> "[java/lang/String")
  var hier: Hierarchy = _

  val lambdaClasses: mutable.ArrayBuffer[(Array[Byte], String)] = new ArrayBuffer[(Array[Byte], String)]()
  val lambdaCounts = new mutable.HashMap[String, Int]
  val lambdaWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES)

  def apply(goal: Goal, ctx: Hierarchy): Seq[(Array[Byte], String)] = {
    hier = ctx
    val cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES)
    val mainClass = visitMain(goal.main, cw)

    goal.classes.foreach(p => lambdaCounts.put(p.ident, 1)) //counting for naming of lambda file
    lambdaCounts.put(goal.main.ident, 1)

    val classes = goal.classes.map(p => (visitClass(p, new ClassWriter(ClassWriter.COMPUTE_FRAMES), ctx.findClass(p.ident).get), p.ident))
    val lambdas = goal.lambdas.map(p => (visitLambda(p, new ClassWriter(ClassWriter.COMPUTE_FRAMES)), p.ident))


    classes :++ lambdas :++ lambdaClasses :+ (mainClass, goal.main.ident)
  }

  def visitMain(clazz: MainClass, cw: ClassWriter): Array[Byte] = {
    cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, clazz.ident, null, "java/lang/Object", null)
    var mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
    mv.visitCode()
    mv.visitVarInsn(Opcodes.ALOAD, 0)
    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
    mv.visitInsn(Opcodes.RETURN)
    mv.visitMaxs(-1, -1)
    mv.visitEnd()

    mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
    visitCode(Seq(clazz.statement), mv, hier.main.findMethod("main").get, new Counter())
    mv.visitInsn(Opcodes.RETURN)
    mv.visitMaxs(-1, -1)
    mv.visitEnd()

    cw.visitEnd()
    cw.toByteArray
  }

  def visitClass(clazz: Clazz, cw: ClassWriter, ctx: CNode): Array[Byte] = {
    //val checker = new CheckClassAdapter(cw)

    cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC, clazz.ident, null, clazz.parent, null)
    val checker = new CheckClassAdapter(cw)

    val mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
    mv.visitVarInsn(Opcodes.ALOAD, 0)
    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, clazz.parent, "<init>", "()V", false)
    mv.visitInsn(Opcodes.RETURN)
    mv.visitMaxs(-1, -1)
    mv.visitEnd()

    clazz.varDecs.foreach(visitField(_, cw, ctx))
    clazz.methods.foreach(p => visitMethod(p, cw, ctx.findMethod(p.ident).get))
    cw.visitEnd()


    cw.toByteArray
  }

  def visitLambda(i: LambdaI, cw: ClassWriter): Array[Byte] = {
    cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT, i.ident, null, "java/lang/Object", null)
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

    //val = new mutable.HashMap[(String, Int), Local]()
    val ctr = new Counter()
    for (i <- meth.params.types.indices) {
      val ident = meth.params.param(i)._2
      val tipe = meth.params.param(i)._1
      //val varNum = ctr.next()
      ctx.locals.put(ident, (tipe, ctr.next()))
    }
    mv.visitCode()
    visitCode(meth.varDecs, mv, ctx, ctr)
    visitCode(meth.statements, mv, ctx, ctr)
    visitRet(meth.ret, meth.tipe, mv, ctx)
    println(meth.ident)
    mv.visitMaxs(0, 0) //args should be ignored and computed automatically
//        try {
//          for (i <- meth.params.types.indices) {
//            val ident = meth.params.param(i)._2
//            val tipe = meth.params.param(i)._1
//            //val varNum = ctr.next()
//            ctx.locals.put(ident, (tipe, ctr.next()))
//          }
//          mv.visitCode()
//          visitCode(meth.varDecs, mv, ctx, ctr)
//          visitCode(meth.statements, mv, ctx, ctr)
//          visitRet(meth.ret, meth.tipe, mv, ctx)
//          println(meth.ident)
//          mv.visitMaxs(0, 0) //args should be ignored and computed automatically
//        }
//        catch {
//          case e:Exception =>
//        }
    mv.visitEnd()
    ctx.locals.clear()
  }

  def visitRet(exp: Expression, tipe: Type, mv: MethodVisitor, ctx: MNode): Unit = {
    visitExp(exp, mv, ctx)
    tipe match {
      case IntType | BoolType => mv.visitInsn(Opcodes.IRETURN)
      case _: IdentType => mv.visitInsn(Opcodes.ARETURN)
      case _: ArrType => mv.visitInsn(Opcodes.ARETURN)
      case VoidType => mv.visitInsn(Opcodes.RETURN)
    }
  }

  def visitCode(statements: Seq[Statement], mv: MethodVisitor, ctx: MNode, ctr: Counter): Unit = {
     for (i <- statements) {
      i match {
        case e: BlockStatement => visitCode(e.value, mv, ctx, ctr)
        case e: VarDeclaration => visitVarDec(e, mv, ctx, ctr)
        case e: Assignment => visitAssign(e, mv, ctx, ctr)
        case e: ArrAssign => visitArrAssign(e, mv, ctx, ctr)
        case e: PrintStatement => visitPrint(e, mv, ctx)
        case e: WhileStatement => visitWhile(e, mv, ctx, ctr)
        case e: IfStatement => visitIf(e, mv, ctx, ctr)
      }
    }
  }

  def visitVarDec(v: VarDeclaration, mv: MethodVisitor, ctx: MNode, ctr: Counter): Unit = {
    //locals.put("ABc",locals.size) //todo might need work
    var ml = 0
    v.tipe match {
      case IntType =>
        ctx.locals.put(v.ident, (IntType, ctr.next()))
      case BoolType =>
        ctx.locals.put(v.ident, (BoolType, ctr.next()))
      case e: IdentType =>
        ctx.locals.put(v.ident, (e, ctr.next()))
      case StringType =>
        ctx.locals.put(v.ident, (StringType, ctr.next()))
      case e: ArrType =>
        ctx.locals.put(v.ident, (e, ctr.next()))
    }
  }

  def visitAssign(a: Assignment, mv: MethodVisitor, ctx: MNode, ctr: Counter): Unit = {
    //val tipe = ctx.fields(a.ident)
    val tipe = ctx.getVar(a.ident).get

    if (ctx.locals.contains(a.ident.toString)) { //todo
      visitExp(a.value, mv, ctx)
      val varNum = ctx.locals(a.ident)._2
      tipe match {
        case IntType | BoolType => mv.visitVarInsn(Opcodes.ISTORE, varNum)
        case _: IdentType => mv.visitVarInsn(Opcodes.ASTORE, varNum)
      }
    }
    else {
      mv.visitVarInsn(Opcodes.ALOAD, 0)
      if (hier.containsLambda(tipe.toString))
        visitLambdaBlock(a.value.asInstanceOf[LambdaBlock], mv, ctx, tipe.asInstanceOf[IdentType])
      else
        visitExp(a.value, mv, ctx)

      val tmp = ctx.parent.get.fields(a.ident)
      val owner = ctx.getOwner(a.ident)
      mv.visitFieldInsn(Opcodes.PUTFIELD, owner.get.ident, a.ident, getDescriptor(tmp))
    }
  }

  def visitArrAssign(a: ArrAssign, mv: MethodVisitor, ctx: MNode, counter: Counter): Unit = {
    if(ctx.locals.contains(a.ident)) {
      val toLoad = ctx.locals(a.ident)
      loadVar(mv,toLoad._1,toLoad._2)
    }
    else {
      val toLoad = ctx.getVar(a.ident)
      val owner = ctx.getOwner(a.ident)
      mv.visitVarInsn(Opcodes.ALOAD,0)
      mv.visitFieldInsn(Opcodes.GETFIELD,owner.get.ident,a.ident,getDescriptor(toLoad.get))
    }

    visitExp(a.offset, mv, ctx)
    visitExp(a.value, mv, ctx)
    mv.visitInsn(Opcodes.IASTORE)

//    val toLoad = ctx.locals
//    visitExp(a.offset, mv, ctx)
//    visitExp(a.value, mv, ctx)
//    if (ctx.locals.contains(a.ident)) {
//      val v = ctx.locals(a.ident)._2
//      //val varNum =((a.ident, v)).varNum
//
//      mv.visitInsn(Opcodes.IASTORE)
//    }
//    else {
//      val tmp = ctx.getVar(a.ident).get
//
//    }
  }

  def visitPrint(statement: PrintStatement, mv: MethodVisitor, ctx: MNode): Unit = {
    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
    visitExp(statement.value, mv, ctx)
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false)
  }

  //issue: SSA causes infinite loop
  def visitWhile(statement: WhileStatement, mv: MethodVisitor, ctx: MNode, ctr: Counter): Unit = {
    val l1 = new Label
    val l3 = new Label
    mv.visitLabel(l1)
    visitExp(statement.condition, mv, ctx) //loads 0 or 1 onto stack for false or true
    mv.visitJumpInsn(Opcodes.IFEQ, l3)
    visitCode(Array[Statement](statement.statement), mv, ctx, ctr) //code inside while
    mv.visitJumpInsn(Opcodes.GOTO, l1)

    mv.visitLabel(l3)
  }

  def visitIf(statement: IfStatement, mv: MethodVisitor, ctx: MNode, ctr: Counter): Unit = {
    val l1, l2 = new Label
    visitExp(statement.condition, mv, ctx)
    mv.visitJumpInsn(Opcodes.IFEQ, l1)
    visitCode(Array[Statement](statement.statement), mv, ctx, ctr)
    mv.visitJumpInsn(Opcodes.GOTO, l2)
    mv.visitLabel(l1)
    visitCode(Array[Statement](statement.elseStatement), mv, ctx, ctr)
    mv.visitLabel(l2)
  }

  //Should evaluate to bytecode and leave result on top of stack
  def visitExp(exp: Expression, mv: MethodVisitor, ctx: MNode): Unit = {
    exp match {
      case e: SumExpression =>
        visitExp(e.left, mv, ctx)
        visitExp(e.right, mv, ctx)
        mv.visitInsn(Opcodes.IADD)
      case e: SubExpression =>
        visitExp(e.left, mv, ctx)
        visitExp(e.right, mv, ctx)
        mv.visitInsn(Opcodes.ISUB)
      case e: MulExpression =>
        visitExp(e.left, mv, ctx)
        visitExp(e.right, mv, ctx)
        mv.visitInsn(Opcodes.IMUL)
      case e: LessExpression => //loads 0 or 1 onto stack
        visitExp(e.left, mv, ctx)
        visitExp(e.right, mv, ctx)
        val l1 = new Label
        val l2 = new Label
        mv.visitJumpInsn(Opcodes.IF_ICMPLT, l1)
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitJumpInsn(Opcodes.GOTO, l2)
        mv.visitLabel(l1)
        mv.visitInsn(Opcodes.ICONST_1)
        mv.visitLabel(l2)
      case e: AndExpression =>
        visitExp(e.left, mv, ctx)
        visitExp(e.right, mv, ctx)
        mv.visitInsn(Opcodes.IAND)
      case e: Ident =>
        if (ctx.locals.contains(e.ident)) {
          val v = ctx.locals(e.ident)
          //val v =((e.ident, ver._2))
          v._1 match {
            case IntType =>
              mv.visitVarInsn(Opcodes.ILOAD, v._2)
            case BoolType =>
              mv.visitVarInsn(Opcodes.ILOAD, v._2)
            case _: IdentType =>
              mv.visitVarInsn(Opcodes.ALOAD, v._2)
            //println(v.varNum)
            case _ => throw new CodeGenerationError
          }
        }
        else {
          mv.visitVarInsn(Opcodes.ALOAD, 0)
          val v = ctx.getVar(e.ident).get
          val owner = ctx.getOwner(e.ident)
          mv.visitFieldInsn(Opcodes.GETFIELD, owner.get.ident, e.ident, getDescriptor(v))
        }
      case e: IntLit =>
        e.value match {
          case 0 | 1 | 2 | 3 | 4 | 5 => mv.visitInsn(Opcodes.ICONST_0 + e.value)
          case _ if e.value < 128 && e.value >= -128 => mv.visitIntInsn(Opcodes.BIPUSH, e.value)
          case _ if e.value >= Short.MinValue && e.value <= Short.MaxValue => mv.visitIntInsn(Opcodes.SIPUSH, e.value)
          case _ => mv.visitLdcInsn(e.value)
        }
      case e: BoolLit =>
        if (e.value)
          mv.visitInsn(Opcodes.ICONST_1)
        else
          mv.visitInsn(Opcodes.ICONST_0)
      case e: ArrAccess =>
        visitExp(e.array, mv, ctx)
        visitExp(e.offset, mv, ctx)
        mv.visitInsn(Opcodes.IALOAD)
      case e: DotExpression =>
        // println(e.funct)
        //mv.visitVarInsn(Opcodes.ALOAD,0)
        visitExp(e.left, mv, ctx)
        val clazz = hier.findClass(e.left.tipe.toString).get
        val meth = clazz.findMethod(e.funct).get
        //e.args.foreach(visitExp(_,mv,ctx))
        for (i <- e.args) {
          visitExp(i, mv, ctx)
        }
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clazz.ident, e.funct, getDescriptor(meth.parameters.types, meth.rType), false)
      case e: LengthExpression =>
        visitExp(e.expression, mv, ctx)
        mv.visitInsn(Opcodes.ARRAYLENGTH)
      case e: ThisExpression =>
        mv.visitVarInsn(Opcodes.ALOAD, 0)
      case e: NewIntArrExpression =>
        visitExp(e.size, mv, ctx)
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT)
      case e: NewIdentExpression =>
        val tipe = hier.findClass(e.ident).get
        mv.visitTypeInsn(Opcodes.NEW, tipe.ident)
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
          tipe.ident,
          "<init>",
          "()V",
          false)
      case e: NegateExpression =>
        visitExp(e.expression, mv, ctx)
        mv.visitInsn(Opcodes.ICONST_1)
        mv.visitInsn(Opcodes.IXOR)
      case e: ParenExpression =>
        visitExp(e.expression, mv, ctx)
      case e: ReturnExpression =>
        visitExp(e.expression, mv, ctx)
        e.tipe match {
          case IntType | BoolType => mv.visitInsn(Opcodes.IRETURN)
          case _: IdentType | _: ArrType => mv.visitInsn(Opcodes.ARETURN)
        }

    }
  }

  def loadVar(mv: MethodVisitor, tipe: Type, varNum: Int): Unit = {
    tipe match {
      case IntType | BoolType => mv.visitVarInsn(Opcodes.ILOAD, varNum)
      case _: IdentType => mv.visitVarInsn(Opcodes.ALOAD, varNum)
      case _: ArrType => mv.visitVarInsn(Opcodes.ALOAD, varNum)
    }
  }

  def visitLambdaBlock(l: LambdaBlock, mv: MethodVisitor, ctx: MNode, tipe: IdentType): Unit = {
    val thisClass = ctx.getThis.ident
    val num = lambdaCounts.get(thisClass)
    lambdaWriter.visit(Opcodes.V11, Opcodes.ACC_PUBLIC, thisClass + "$" + num.get, null, tipe.ident, null)
  }

  def getDescriptor(t: Type): String = {
    t match {
      case IntType => "I"
      case VoidType => "V"
      case BoolType => "Z"
      case StringType => "Ljava/lang/String;"
      case e: ArrType => "[" + getDescriptor(e.tipe)
      case e: IdentType => "L" + e.ident + ";"
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
