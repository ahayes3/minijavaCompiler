import org.objectweb.asm.{ClassWriter, Label, MethodVisitor, Opcodes}

import scala.collection.immutable.HashMap
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

class CodeGenerationError extends Exception{}
//NOTES
//Each lambda instance should generate a .class and put the bytes in lambdaClasses. The name will follow the name of the name of the class it is found in with $(number) appended

object GenerateCode {
  val typeMap: Map[Type, String] = HashMap(IntType -> "I", BoolType -> "Z", ArrType(IntType) -> "[I", ArrType(StringType) -> "[java/lang/String")
  var hier: Hierarchy = _

  val lambdaClasses: mutable.ArrayBuffer[(Array[Byte], String)] = new ArrayBuffer[(Array[Byte], String)]()
  val lambdaCounts = new mutable.HashMap[String, Int]
  val lambdaWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)

  def apply(goal: Goal, ctx: Hierarchy): Seq[(Array[Byte], String)] = {
    hier = ctx
    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS)
    val mainClass = visitMain(goal.main, cw)

    goal.classes.foreach(p => lambdaCounts.put(p.ident, 1)) //counting for naming of lambda file
    lambdaCounts.put(goal.main.ident, 1)

    val classes = goal.classes.map(p => (visitClass(p, new ClassWriter(ClassWriter.COMPUTE_MAXS), ctx.findClass(p.ident).get), p.ident))
    val lambdas = goal.lambdas.map(p => (visitLambda(p, new ClassWriter(ClassWriter.COMPUTE_MAXS)), p.ident))

    classes :++ lambdas :++ lambdaClasses :+ (mainClass,goal.main.ident)
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
    visitCode(Seq(clazz.statement),mv,hier.main.findMethod("main").get,new mutable.HashMap(),new Counter())
    mv.visitInsn(Opcodes.RETURN)
    mv.visitMaxs(-1, -1)
    mv.visitEnd()

    cw.visitEnd()
    cw.toByteArray
  }

  def visitClass(clazz: Clazz, cw: ClassWriter, ctx: CNode): Array[Byte] = {
    cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC, clazz.ident, null, clazz.parent, null)

    val mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>","()V",null,null)
    mv.visitVarInsn(Opcodes.ALOAD,0)
    mv.visitMethodInsn(Opcodes.INVOKESPECIAL,clazz.parent,"<init>","()V",false)
    mv.visitInsn(Opcodes.RETURN)
    mv.visitMaxs(-1,-1)
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

    val versions = new mutable.HashMap[(String,Int),Local]()
    val ctr = new Counter()
    //mv.visitVarInsn(Opcodes.ALOAD, 0) //loads "this"

    for(i <- meth.params.types.indices) {
      val ident = meth.params.param(i)._2
      val tipe = meth.params.param(i)._1
      val varNum = ctr.next()
      versions.put((ident,0),Local(varNum,null))

      ctx.locals.put(ident,(tipe,0))
//      tipe match {
//        case IntType | BoolType => mv.visitVarInsn(Opcodes.ISTORE, varNum)
//        case _:IdentType => mv.visitVarInsn(Opcodes.ASTORE, varNum)
//        case _:ArrType => mv.visitVarInsn(Opcodes.ASTORE,varNum)
//      }
    }
    mv.visitCode()
    visitCode(meth.varDecs,mv,ctx,versions,ctr)
    visitCode(meth.statements, mv, ctx, versions,ctr)
    visitRet(meth.ret,meth.tipe,mv,ctx,versions)
    mv.visitMaxs(-1, -1) //args should be ignored and computed automatically
    mv.visitEnd()
    ctx.locals.clear()
  }

  def visitRet(exp: Expression,tipe:Type, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local]): Unit = {
    visitExp(exp,mv,ctx,versions)
    tipe match {
      case IntType | BoolType => mv.visitInsn(Opcodes.IRETURN)
      case _:IdentType => mv.visitInsn(Opcodes.ARETURN)
      case _:ArrType => mv.visitInsn(Opcodes.ARETURN)
      case VoidType => mv.visitInsn(Opcodes.RETURN)
    }
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
    //val tipe = ctx.fields(a.ident)
    val tipe = ctx.getVar(a.ident).get
//    if (hier.containsLambda(tipe.toString))
//      visitLambdaBlock(a.value.asInstanceOf[LambdaBlock], mv, ctx, versions, tipe.asInstanceOf[IdentType])
//    else
//      visitExp(a.value, mv, ctx, versions)
    //val version = ctr.next()
    if(ctx.locals.contains(a.ident)) {
      if (hier.containsLambda(tipe.toString))
        visitLambdaBlock(a.value.asInstanceOf[LambdaBlock], mv, ctx, versions, tipe.asInstanceOf[IdentType])
      else
        visitExp(a.value, mv, ctx, versions)
      val version = ctx.locals(a.ident)._2
      val varNum = if(!versions.contains((a.ident,version))) {
        val tmp=ctr.next()
        versions.put((a.ident,0),Local(tmp,null))
        tmp
      }
      else {
        if (hier.containsLambda(tipe.toString))
          visitLambdaBlock(a.value.asInstanceOf[LambdaBlock], mv, ctx, versions, tipe.asInstanceOf[IdentType])
        else
          visitExp(a.value, mv, ctx, versions)

        val v = ctx.locals(a.ident)._2
        val tmp = ctr.next()
        ctx.locals.remove(a.ident)
        //val old = versions.keys.find(p => p._1 == a.ident && p._2 == version).get
        versions.put((a.ident, v+1), Local(tmp, a.value))
        ctx.locals.put(a.ident, (tipe, v+1))
        tmp
      }
      //val varNum = versions(a.ident,version).varNum
      tipe match {
        case IntType | BoolType => mv.visitVarInsn(Opcodes.ISTORE,varNum)
        case _: IdentType => mv.visitVarInsn(Opcodes.ASTORE,varNum)
      }

      //val newversion = ctx.locals(a.ident)._2 + 1

    }
    else { //in this case it is a class field
      mv.visitVarInsn(Opcodes.ALOAD,0)
      if (hier.containsLambda(tipe.toString))
        visitLambdaBlock(a.value.asInstanceOf[LambdaBlock], mv, ctx, versions, tipe.asInstanceOf[IdentType])
      else
        visitExp(a.value, mv, ctx, versions)

      val tmp = ctx.getVar(a.ident)
      val owner = ctx.getOwner(a.ident)
      mv.visitFieldInsn(Opcodes.PUTFIELD,owner.get.ident,tmp.get.toString,getDescriptor(tmp.get))
    }
  }

  def visitArrAssign(a: ArrAssign, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local], counter: Counter): Unit = {
    visitExp(a.offset, mv, ctx, versions)
    visitExp(a.value, mv, ctx, versions)
    if(ctx.locals.contains(a.ident)) {
      val v = ctx.locals(a.ident)._2
      val varNum = versions((a.ident,v)).varNum

      mv.visitVarInsn(Opcodes.IASTORE,varNum)
    }
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
    mv.visitInsn(Opcodes.POP)
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
        if(ctx.locals.contains(e.ident)) {
          val ver = ctx.locals(e.ident)
          val v = versions((e.ident, ver._2))
          ver._1 match {
            case IntType =>
              mv.visitVarInsn(Opcodes.ILOAD, v.varNum)
            case BoolType =>
              mv.visitVarInsn(Opcodes.ILOAD, v.varNum)
            case _: IdentType =>
              mv.visitVarInsn(Opcodes.ALOAD, v.varNum)
              println(v.varNum)
            case _ => throw new CodeGenerationError
          }
        }
        else {
          val v = ctx.getVar(e.ident).get
          val owner = ctx.getOwner(e.ident).get
          mv.visitFieldInsn(Opcodes.GETFIELD,owner.ident,e.ident,getDescriptor(v))
        }
      case e: IntLit =>
//        if(e.value <=Short.MaxValue && e.value >= Short.MinValue)
//          mv.visitIntInsn(Opcodes.SIPUSH, e.value)
//        else if(e.value <128 && e.value >= -128)
//          mv.visitIntInsn(Opcodes.BIPUSH,e.value)
//        else if(e.value ==0 || e.value ==1 || e.value ==2 || e.value ==3 || e.value ==4 || e.value ==5)
//          mv.visitInsn(Opcodes.ICONST_0+e.value)
//        else
//          mv.visitLdcInsn(e.value)
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
        visitExp(e.array, mv, ctx, versions)
        visitExp(e.offset, mv, ctx, versions)
        mv.visitInsn(Opcodes.IALOAD)
      case e: DotExpression =>
        println(e.funct)
        //mv.visitVarInsn(Opcodes.ALOAD,0)
        visitExp(e.left,mv,ctx,versions)
        val clazz = hier.findClass(e.left.tipe.toString).get
        val meth = clazz.findMethod(e.funct).get
        //e.args.foreach(visitExp(_,mv,ctx,versions))
        for(i <- e.args) {
          visitExp(i,mv, ctx, versions)
        }
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,clazz.ident,e.funct,getDescriptor(meth.parameters.types,meth.rType),false)
      case e: LengthExpression =>
        visitExp(e.expression, mv, ctx, versions)
        mv.visitInsn(Opcodes.ARRAYLENGTH)
      case e: ThisExpression =>
        mv.visitVarInsn(Opcodes.ALOAD, 0)
      case e: NewIntArrExpression =>
        visitExp(e.size, mv, ctx, versions)
        mv.visitInsn(Opcodes.NEWARRAY)
      case e: NewIdentExpression =>
        val tipe = hier.findClass(e.ident).get
        mv.visitTypeInsn(Opcodes.NEW,tipe.ident)
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
          tipe.ident,
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
