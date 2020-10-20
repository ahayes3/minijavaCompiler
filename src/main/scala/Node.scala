//Classes for Tree of AST
trait Node
trait Type extends Node
trait Statement extends Node
trait Expression extends Node

case class Goal(main:MainClass,classes: Array[Clazz])
case class Clazz(ident:String,parent:Option[String],varDecs:Array[VarDeclaration],methods:Array[MethodDec]) extends Node
case class MainClass(ident:String,strArgsIdent:String, statement:Statement) extends Node

case class MethodDec(tipe:Type,ident:Ident, params:Parameters, varDecs: Array[Assignment], statements:Array[Statement], ret:Expression)

case object IntType extends Type
case object BoolType extends Type
case object IntArrType extends Type
case object StringArrType extends Type
case class IdentType(ident:String) extends Type

trait BinaryExpression extends Expression {
  val left: Expression
  val right:Expression
}

case class Parameters(param: (Type, String)*)

case class SumExpression(left:Expression,right:Expression) extends Expression
case class SubExpression(left:Expression,right:Expression) extends Expression
case class MulExpression(left:Expression,right:Expression) extends Expression
case class DivExpression(left:Expression,right:Expression) extends Expression
case class Ident(varName:String) extends Expression
case class IntLit(value:Int) extends Expression
case class BoolLit(value:Boolean) extends Expression
case class ArrAccess(array:Expression, offset:Expression)
case class DotExpression(left:Expression,funct:String,param:Parameters)
case class LambdaExpression(params:Parameters, statement:Statement)
case class LengthExpression(varName:String)
case class ThisExpression(clazz:Clazz)
case class NewIntArrExpression(size:Expression)
case class NewIdentExpression(ident:String)
case class NegateExpression(expression:Expression)
case class ParenExpression(expression:Expression)

case class VarDeclaration(varName:String, value:Expression) extends Statement
case class Assignment(varName:String, value:Expression) extends Statement
case class ArrAssign(varName:String,offset:Expression,value:Expression) extends Statement
case class PrintStatement(value:Expression) extends Statement
case class WhileStatement(condition:Expression,statement:Statement) extends Statement
case class IfStatement(condition:Expression,statement:Statement) extends Statement



