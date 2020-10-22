//Classes for Tree of AST
trait Node
trait Type
trait Statement extends Node
trait Expression extends Node
trait Lambda extends Expression

case class Goal(main:MainClass,classes: Array[Clazz],line:Int) extends Node
case class Clazz(ident:String,parent:Option[String],varDecs:Array[VarDeclaration],methods:Array[MethodDec],line:Int) extends Node
case class MainClass(ident:String,strArgsIdent:String, statement:Statement,line:Int) extends Node

case class MethodDec(tipe:Type,ident:String, params:Parameters, varDecs: Array[VarDeclaration], statements:Array[Statement], ret:Expression,line:Int) extends Node

case object IntType extends Type
case object BoolType extends Type
case object IntArrType extends Type
case object StringArrType extends Type
case class IdentType(ident:String) extends Type

trait BinaryExpression extends Expression {
  val left: Expression
  val right:Expression
}

case class Parameters(param: Array[(Type, String)])

trait MathExpression extends Expression {
  val left:Expression
  val right:Expression
  val line:Int
}
case class SumExpression(left:Expression,right:Expression,line:Int) extends MathExpression
case class SubExpression(left:Expression,right:Expression,line:Int) extends MathExpression
case class MulExpression(left:Expression,right:Expression,line:Int) extends MathExpression
case class LessExpression(left:Expression,right:Expression,line:Int) extends MathExpression
case class AndExpression(left:Expression,right:Expression,line:Int) extends MathExpression
case class Ident(ident:String, line:Int) extends Expression
case class IntLit(value:Int,line:Int) extends Expression
case class BoolLit(value:Boolean,line:Int) extends Expression
case class ArrAccess(array:Expression, offset:Expression,line:Int) extends Expression
case class DotExpression(left:Expression,funct:String,expressions: Array[Expression],line:Int) extends Expression
case class LengthExpression(expression:Expression,line:Int) extends Expression
case class ThisExpression(line:Int) extends Expression
case class NewIntArrExpression(size:Expression,line:Int) extends Expression
case class NewIdentExpression(ident:String,line:Int) extends Expression
case class NegateExpression(expression:Expression,line:Int) extends Expression
case class ParenExpression(expression:Expression,line:Int) extends Expression
case class ReturnExpression(expression:Expression,line:Int) extends Expression

case class LambdaExpression(params:Parameters,expression:Expression,line:Int) extends Lambda
case class LambdaBlock(params:Parameters,vars:Array[VarDeclaration],statements:Array[Statement],returnExp:Expression,line:Int) extends Lambda

case class BlockStatement(value: Array[Statement],line:Int) extends Statement
case class VarDeclaration(tipe: Type, ident:String, line:Int) extends Statement
case class Assignment(ident:String, value:Expression, line:Int) extends Statement
case class ArrAssign(ident:String, offset:Expression, value:Expression, line:Int) extends Statement
case class PrintStatement(value:Expression,line:Int) extends Statement
case class WhileStatement(condition:Expression,statement:Statement,line:Int) extends Statement
case class IfStatement(condition:Expression,statement:Statement,elseStatement:Statement,line:Int) extends Statement



