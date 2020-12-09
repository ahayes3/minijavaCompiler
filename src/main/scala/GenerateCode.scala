import org.objectweb.asm.{ClassWriter, MethodVisitor, Opcodes}
import scala.collection.immutable.HashMap
import scala.collection.mutable
case class Local(num:Int,value:Any)
case class Counter() {
  var c = 0
  def add(): Unit = {
    c +=1
  }
  def get(): Int = {
    c
  }
  def next(): Int = {
    val a =c
    c+=1
    a
  }
}
object GenerateCode {
  val typeMap: Map[Type, String] = HashMap(IntType -> "I", BoolType -> "Z", ArrType(IntType) -> "[I", ArrType(StringType) -> "[java/lang/String")
  var hier:Hierarchy = _
  def apply(goal: Goal,ctx:Hierarchy): Seq[(Array[Byte],String)] = {
    hier = ctx
    val cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES)
    visitMain(goal.main, cw)
    val classes = goal.classes.map(p => (visitClass(p, cw,ctx.findClass(p.ident).get),p.ident))
    val lambdas = goal.lambdas.map(p => (visitLambda(p,cw),p.ident))
    classes :++lambdas
  }
  def visitMain(clazz: MainClass, cw: ClassWriter): Array[Byte] = {
    cw.visit(Opcodes.V13, Opcodes.ACC_PUBLIC, clazz.ident, null, "java/main/Object", null)
    cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,"main","([Ljava/lang/String)V",null,null)
    cw.visitEnd()
    cw.toByteArray
  }
  def visitClass(clazz: Clazz, cw: ClassWriter,ctx:CNode): Array[Byte] = {
    cw.visit(Opcodes.V13, Opcodes.ACC_PUBLIC, clazz.ident, null, clazz.parent, null)
    clazz.varDecs.foreach(visitField(_, cw,ctx))
    clazz.methods.foreach(p => visitMethod(p, cw,ctx.findMethod(p.ident).get))
    cw.visitEnd()
    cw.toByteArray
  }
  def visitLambda(i: LambdaI, cw: ClassWriter):Array[Byte] = {
    //todo
    ???
  }
  def visitField(field: VarDeclaration, cw: ClassWriter,ctx:CNode): Unit = {
    cw.visitField(Opcodes.ACC_PUBLIC, field.ident, getDescriptor(field.tipe), null, null).visitEnd()
  }

  def visitMethod(meth: MethodDec, cw: ClassWriter,ctx:MNode): Unit = {
    val mv = cw.visitMethod(Opcodes.ACC_PUBLIC, meth.ident, getDescriptor(meth.params.types, meth.tipe), null, null)
    mv.visitVarInsn(Opcodes.ALOAD,0) //loads "this"
    mv.visitCode()
    visitCode(meth.statements, mv,ctx,new mutable.HashMap[(String,Int),Local](),Counter())
    mv.visitMaxs(-1,-1) //args should be ignored and computed automatically
    mv.visitEnd()
  }

  def visitCode(statements: Seq[Statement], mv: MethodVisitor,ctx:MNode,versions:mutable.HashMap[(String,Int),Local],ctr:Counter): Unit = { //todo
    for (i <- statements) {
      i match {
        case e: BlockStatement => visitCode(e.value,mv,ctx,versions,ctr)
        case e: VarDeclaration => visitVarDec(e,mv,ctx,versions,ctr)
        case e: Assignment =>  visitAssign(e,mv,ctx,versions,ctr)
        case e: ArrAssign =>
        case e: PrintStatement => visitPrint(e,mv,ctx,versions)
        case e: PrintStatement =>
        case e: WhileStatement =>
        case e: IfStatement =>
        //todo insns for statements
      }
    }
  }
  def visitVarDec(v: VarDeclaration, mv: MethodVisitor,ctx:MNode,versions:mutable.HashMap[(String,Int),Local],ctr:Counter): Unit = {
    //locals.put("ABc",locals.size) //todo
    var ml=0
    v.tipe match {
      case IntType =>
        ctx.locals.put(v.ident,(IntType,0))
      case BoolType =>
        ctx.locals.put(v.ident,(BoolType,0))
      case e:IdentType =>
        ctx.locals.put(v.ident,(e,0))
      case StringType =>
        ctx.locals.put(v.ident,(StringType,0))
      case e:ArrType =>
        ctx.locals.put(v.ident,(e,0))
    }
  }
  def visitAssign(a: Assignment, mv: MethodVisitor, ctx: MNode, versions:mutable.HashMap[(String,Int),Local],ctr:Counter): Unit = {
    val tipe = ctx.fields.get(a.ident)
    visitExp(a.value,mv,ctx,versions)
    versions.put((a.ident,ctr.next()),Local(ctr.next(),a.value))
  }
  def visitPrint(statement: PrintStatement, mv: MethodVisitor, ctx: MNode, versions: mutable.HashMap[(String, Int), Local]): Unit = {
    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System","out", "Ljava/io/PrintStream;")
    visitExp(statement.value,mv,ctx,versions)
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(I)V",false)
  }

  //Should evaluate to bytecode and leave result on top of stack
  def visitExp(exp: Expression,mv:MethodVisitor,ctx:MNode,versions:mutable.HashMap[(String,Int),Local]): Unit = {
    exp match {
      case e:SumExpression =>
        mv.visitIntInsn(Opcodes.)
      case e:SubExpression =>
      case e:MulExpression =>
      case e:LessExpression =>
      case e:AndExpression =>
      case e:Ident =>
      case e:IntLit =>
      case e:BoolLit =>
      case e:ArrAccess =>
      case e:DotExpression =>
      case e:LengthExpression =>
      case e:ThisExpression =>
      case e:NewIntArrExpression =>
      case e:NewIdentExpression =>
      case e:NegateExpression =>
      case e:ParenExpression =>
      case e:ReturnExpression =>
      case e:LambdaExpression =>
      case e:LambdaBlock =>
    }
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
