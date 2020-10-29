//Classes for Tree of AST
trait Node {
  val line:Int
  def getChildren:Seq[Node]
}
trait Type
trait Statement extends Node
trait Expression extends Node
trait Lambda extends Expression

case class Goal(main:MainClass,classes: Seq[Clazz],lambdas:Seq[LambdaI],line:Int) extends Node {
  override def getChildren: Seq[Node] = {
    Seq(main) :++ classes
  }
}
case class Clazz(ident:String,parent:Option[String],varDecs:Seq[VarDeclaration],methods:Seq[MethodDec],line:Int) extends Node {
  override def getChildren: Seq[Node] = {
    varDecs :++ methods
  }
}
case class MainClass(ident:String,strArgsIdent:String, statement:Statement,line:Int) extends Node {
  override def getChildren: Seq[Node] = {
    Seq(statement)
  }
}

case class LambdaI(ident:String,methodIdent:String,mTipe:Type,params:Parameters,line:Int) extends Node {
  override def getChildren: Seq[Node] = Seq(params)
}

case class MethodDec(tipe:Type,ident:String, params:Parameters, varDecs: Seq[VarDeclaration], statements:Seq[Statement], ret:Expression,line:Int) extends Node {
  override def getChildren: Seq[Node] = {
    Seq(params) :++ varDecs :++ statements :+ ret
  }
}

case object SomeType extends Type
case object IntType extends Type
case object BoolType extends Type
case object IntArrType extends Type
case object StringArrType extends Type
case class IdentType(ident:String) extends Type

trait BinaryExpression extends Expression {
  val left: Expression
  val right:Expression
}

case class Parameters(param: Seq[(Type, String)],line:Int) extends Node {
  override def getChildren: Seq[Node] =
    Seq()
}

trait MathExpression extends Expression {
  val left:Expression
  val right:Expression
  val line:Int

  override def getChildren: Seq[Node] = {
    Seq(left,right)
  }
}

case class SumExpression(left:Expression,right:Expression,line:Int) extends MathExpression
case class SubExpression(left:Expression,right:Expression,line:Int) extends MathExpression
case class MulExpression(left:Expression,right:Expression,line:Int) extends MathExpression
case class LessExpression(left:Expression,right:Expression,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq(left,right)
}
case class AndExpression(left:Expression,right:Expression,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq(left,right)
}
case class Ident(ident:String, line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq()
}
case class IntLit(value:Int,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq()
}
case class BoolLit(value:Boolean,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq()
}
case class ArrAccess(array:Expression, offset:Expression,line:Int) extends Expression {
  override def getChildren: Seq[Node] = {
    Seq(array,offset)
  }
}
case class DotExpression(left:Expression,funct:String,args: Seq[Expression],line:Int) extends Expression {
  override def getChildren: Seq[Node] = {
    Seq(left) :++ args
  }
}
case class LengthExpression(expression:Expression,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq(expression)
}
case class ThisExpression(line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq()
}
case class NewIntArrExpression(size:Expression,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq(size)
}
case class NewIdentExpression(ident:String,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq()
}
case class NegateExpression(expression:Expression,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq(expression)
}
case class ParenExpression(expression:Expression,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq(expression)
}
case class ReturnExpression(expression:Expression,line:Int) extends Expression {
  override def getChildren: Seq[Node] = Seq(expression)
}

case class LambdaExpression(params:Parameters,expression:Expression,line:Int) extends Lambda {
  override def getChildren: Seq[Node] = Seq(params,expression)
}
case class LambdaBlock(params:Parameters,vars:Seq[VarDeclaration],statements:Seq[Statement],returnExp:Expression,line:Int) extends Lambda {
  override def getChildren: Seq[Node] = Seq(params) :++vars :++ statements :+ returnExp
}

case class BlockStatement(value: Seq[Statement],line:Int) extends Statement {
  override def getChildren: Seq[Node] = value
}
case class VarDeclaration(tipe: Type, ident:String, line:Int) extends Statement {
  override def getChildren: Seq[Node] = Seq()
}
case class Assignment(ident:String, value:Expression, line:Int) extends Statement {
  override def getChildren: Seq[Node] = Seq(value)
}
case class ArrAssign(ident:String, offset:Expression, value:Expression, line:Int) extends Statement {
  override def getChildren: Seq[Node] = Seq(offset,value)
}
case class PrintStatement(value:Expression,line:Int) extends Statement {
  override def getChildren: Seq[Node] = Seq(value)
}
case class WhileStatement(condition:Expression,statement:Statement,line:Int) extends Statement {
  override def getChildren: Seq[Node] = Seq(condition,statement)
}
case class IfStatement(condition:Expression,statement:Statement,elseStatement:Statement,line:Int) extends Statement {
  override def getChildren: Seq[Node] = Seq(condition,statement,elseStatement)
}



