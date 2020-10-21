//Classes for Tree of AST
trait Node
trait Type extends Node
trait Statement extends Node
trait Expression extends Node
trait Lambda extends Expression

case class Goal(main:MainClass,classes: Array[Clazz])
case class Clazz(ident:String,parent:Option[String],varDecs:Array[VarDeclaration],methods:Array[MethodDec]) extends Node
case class MainClass(ident:String,strArgsIdent:String, statement:Statement) extends Node

case class MethodDec(tipe:Type,ident:String, params:Parameters, varDecs: Array[VarDeclaration], statements:Array[Statement], ret:Expression)

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

case class SumExpression(left:Expression,right:Expression) extends Expression
case class SubExpression(left:Expression,right:Expression) extends Expression
case class MulExpression(left:Expression,right:Expression) extends Expression
case class LessExpression(left:Expression,right:Expression) extends Expression
case class AndExpression(left:Expression,right:Expression) extends Expression
//case class DivExpression(left:Expression,right:Expression) extends Expression
case class Ident(varName:String) extends Expression
case class IntLit(value:Int) extends Expression
case class BoolLit(value:Boolean) extends Expression
case class ArrAccess(array:Expression, offset:Expression) extends Expression
case class DotExpression(left:Expression,funct:String,expressions: Array[Expression]) extends Expression
case class LengthExpression(expression:Expression) extends Expression
case class ThisExpression() extends Expression
case class NewIntArrExpression(size:Expression) extends Expression
case class NewIdentExpression(ident:String) extends Expression
case class NegateExpression(expression:Expression) extends Expression
case class ParenExpression(expression:Expression) extends Expression
case class ReturnExpression(expression:Expression) extends Expression

case class LambdaExpression(params:Parameters,expression:Expression) extends Lambda
case class LambdaBlock(params:Parameters,vars:Array[VarDeclaration],statements:Array[Statement],returnExp:Expression) extends Lambda

case class BlockStatement(value: Array[Statement]) extends Statement
case class VarDeclaration(tipe: Type, varName:String) extends Statement
case class Assignment(varName:String, value:Expression) extends Statement
case class ArrAssign(varName:String,offset:Expression,value:Expression) extends Statement
case class PrintStatement(value:Expression) extends Statement
case class WhileStatement(condition:Expression,statement:Statement) extends Statement
case class IfStatement(condition:Expression,statement:Statement,elseStatement:Statement) extends Statement



