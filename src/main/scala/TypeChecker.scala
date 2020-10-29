import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class TypeCheckerError extends Error

case class VarStack(prev: VarStack, vars: HashMap[String, Type])

object TypeChecker {

  case class InheritanceNode(name: String, parent: Option[InheritanceNode], children: mutable.ArrayBuffer[InheritanceNode]) {
    def isParent(ident: String): Boolean = {
      if (parent.getOrElse(return false).name == ident)
        true
      else
        parent.get.isParent(ident)
    }

    def find(ident: String): Option[InheritanceNode] = {
      if (this.name == ident)
        Option(this)
      else if (this.children.nonEmpty)
        this.children.find(_.find(ident).nonEmpty)
      else
        None
    }
  }

  def typeEqual(t1: Type, t2: Type): Boolean = {
    if (t1 == t2 || t1 == SomeType || t2 == SomeType)
      true
    else
      false
  }

  var classSymbols: mutable.HashMap[String, Environment] = _
  var inheritance: InheritanceNode = _
  var classes = new ArrayBuffer[String]()

  def apply(ast: Goal, symbols: mutable.HashMap[String, Environment]): Seq[String] = {
    inheritance = InheritanceNode("Object", None, new ArrayBuffer[InheritanceNode]())
    classSymbols = symbols
    classes.clear()

    visit(ast, null)
  }

  def visit(node: Node, env: Environment): Seq[String] = {
    node match {
      case e: Goal => visit(e.main, null) :++ e.classes.flatMap(visit(_, null))
      case e: MainClass =>
        val newEnv = new Environment(null, e.ident)
        newEnv.addVar(e.strArgsIdent, StringArrType)
        classes.addOne(e.ident)
        visit(e.statement, newEnv)

      case e: Clazz =>
        val newEnv = new Environment(null, e.ident)
        var a = e.varDecs.flatMap(visit(_, newEnv))
        a = a :++ e.methods.flatMap(visit(_, newEnv))
        if (classes.contains(e.ident))
          a = a :+ (e.line + ":Error: Duplicate class.")
        a

      case e: MethodDec =>
        val newEnv = new Environment(env, "")
        e.params.param.foreach(p=> newEnv.addVar(p._2,p._1))
        var a = e.varDecs.flatMap(visit(_, newEnv))
        a = a :++ e.statements.flatMap(visit(_, newEnv))
        if (env.methods.contains((e.ident, e.params)))
          a = a :+ (e.line + ":Error: Duplicate method.")
        else
          env.addMethod(e.ident, e.params, e.tipe)
        val (b, t1) = visitExp(e.ret, newEnv)
        a = a :++ b
        if (t1 != e.tipe && t1 != SomeType)
          a = a :+ (e.ret.line + ":Error: expected " + e.tipe + " found " + t1 + ".")
        a

      case e: VarDeclaration =>
        env.addVar(e.ident, e.tipe)
        Seq()

      case e: BlockStatement =>
        e.value.flatMap(visit(_, env))

      case e: Assignment =>
        val (b, tipe) = visitExp(e.value, env)

        var a = Seq[String]()
        a = a :++ b
        val t = env.getVarType(e.ident)
        if (t != tipe)
          a = a :+ (e.line + ":Error: Mismatched types " + t + " and " + tipe + ".")
        a

      case e: ArrAssign =>
        val t0 = env.getVarType(e.ident)
        var a = Seq[String]()
        if (t0 != IntArrType)
          a = a :+ (e.line + ":Error: expected int[] found " + t0 + ".")
        val (b, t1) = visitExp(e.offset, env)
        a = a :++ b
        if (t1 != IntType)
          a = a :+ (e.line + ":Error: expected int.")
        val (c, t2) = visitExp(e.value, env)
        a = a :++ c
        if (t2 != t0)
          a = a :+ (e.line + ":Error: expected " + t0 + " found " + t2 + ".")
        a

      case e: PrintStatement =>
        val (b, t1) = visitExp(e.value, env)
        if (t1 != IntType)
          b :+ (e.line + ":Error: expected int found " + t1 + ".")
        else
          b

      case e: WhileStatement =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.condition, env)
        a = a :++ b
        if (t1 != BoolType)
          a = a :+ (e.line + ":Error: expected boolean found " + t1 + ".")
        visit(e.statement, env)

      case e: IfStatement =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.condition, env)
        a = a :++ b
        if (t1 != BoolType)
          a = a :+ (e.line + ":Error: expected boolean found " + t1 + ".")
        a = a :++ visit(e.statement, env)
        a :++ visit(e.elseStatement, env)

      case _ => throw new NotImplementedError("Shouldn't be here")
    }
  }

  def visitExp(exp: Expression, env: Environment): (Seq[String], Type) = {
    exp match {
      case e: MathExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.left, env)
        val (c, t2) = visitExp(e.right, env)
        a = a :++ b :++ c
        val operation = e match {
          case _:SumExpression => "add"
          case _:SubExpression => "subtract"
          case _:MulExpression => "multiply"
        }
        if (t1 != IntType || t2 != IntType)
          a = a :+ (e.line + ":Error: Cannot" +operation+ " to " + t1 + " and " + t2 + ".")
        (a, IntType)
      case e:LessExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.left,env)
        val (c,t2) = visitExp(e.right,env)
        a = a :++ b :++ c
        if((t1 != IntType && t1 != SomeType) || (t2 != IntType && t2 != SomeType))
          a = a :+ (e.line + ":Error: Cannot apply < to " + t1 + " and " + t2 + ".")
        (a,BoolType)
      case e:AndExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.left,env)
        val (c,t2) = visitExp(e.right,env)
        a = a :++ b :++ c
        if((t1 != BoolType && t1 != SomeType) || (t2 != BoolType && t2 != SomeType))
          a = a :+ (e.line + ":Error: Cannot apply && to " + t1 + " and " + t2 + ".")
        (a,BoolType)
      case e: Ident =>
        (Seq(), env.getVarType(e.ident))
      case _: IntLit =>
        (Seq(), IntType)
      case _: BoolLit =>
        (Seq(), BoolType)
      case e: ArrAccess =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.array, env)
        a = a :++ b
        if (t1 != IntArrType)
          a = a :+ (e.line + ":Error: expected Seq found " + t1 + ".")
        val (c, t2) = visitExp(e.array, env)
        a = a :++ c
        if (t2 != IntType)
          a = a :+ (e.line + ":Error: expected int found " + t2 + ".")
        (a, IntArrType)
      case e: DotExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.left, env)
        a = a :++ b
        if (!t1.isInstanceOf[IdentType])
          a :+ (e.line + ":Error: expected class found " + t1 + ".")
        val typeArr = e.args.map(p => {
          val (b, t0) = visitExp(p, env)
          a = a :++ b
          t0
        })
        val outType = t1 match {
          case i: IdentType => classSymbols(i.ident).getMethodType(e.funct, typeArr).getOrElse(throw new TypeCheckerError)
          case _ => SomeType
        }
        //val outType = if(t1.isInstanceOf[IdentType]) classSymbols.get(t1.asInstanceOf[IdentType].ident).get.getMethodType(e.funct,typeArr)
        (a, outType)

      case e: LengthExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.expression, env)
        a = a :++ b
        if (t1 != IntArrType)
          a = a :+ (e.line + ":Error: expected int[] found " + t1 + ".")
        (a, IntType)
      case _: ThisExpression =>
        (Seq(), IdentType(env.getBottom.ident))
      case e: NewIntArrExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.size, env)
        a = a :++ b
        if (t1 != IntType)
          a = a :+ (e.line + ":Error: expected int found " + t1 + ".")
        (a, IntArrType)
      case e: NewIdentExpression =>
        if (!classSymbols.contains(e.ident))
          (Seq(e.line + ":Error: class " + e.ident + " not found."), SomeType)
        else
          (Seq(), IdentType(e.ident))
      case e: NegateExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.expression, env)
        a = a :++ b
        if (t1 != IntType && t1 != BoolType)
          a = a :+ (e.line + ":Error: expected (int,boolean) found " + t1 + ".")
        (a, t1)
      case e: ParenExpression =>
        val a = Seq[String]()
        val (b, t1) = visitExp(e.expression, env)
        (a :++ b, t1)
      case e: ReturnExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.expression, env)
        a = a :++ b
        (a, t1)
      case e: LambdaExpression =>
        val (b, t1) = visitExp(e.expression, env)
        (b, t1)
      case e: LambdaBlock =>
        val newEnv = new Environment(env, "")
        var a = e.vars.flatMap(p => visit(p, newEnv))
        a = a :++ e.statements.flatMap(p => visit(p, newEnv))
        val (b, t1) = visitExp(e.returnExp, newEnv)
        (a :++ b, t1)

      case _ => throw new NotImplementedError("Shouldn't be here")
    }
  }
}
