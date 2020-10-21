import java.text.ParseException

import MinijavaParser._
import org.antlr.v4.runtime.tree.TerminalNodeImpl

import scala.collection.mutable
import scala.jdk.CollectionConverters.ListHasAsScala

object AstBuilder {
  var head:Node = _

  def apply(ctx: GoalContext): Node = {
    var mainclass:MainClass = null
    var classes = Array[Clazz]()

    var children = ctx.children.asScala
    mainclass = mainClass(children.head.asInstanceOf[MainClassContext])
    children = children.tail

    if(children.last.isInstanceOf[TerminalNodeImpl])
      children.remove(children.size - 1)

    for(a <- children) {
      classes = classes :+ clazz(a.asInstanceOf[ClassDeclarationContext])
    }
    null
  }
  def mainClass(ctx:MainClassContext): MainClass = {
    val children = ctx.children.asScala
    MainClass(children.find(_.isInstanceOf[IdentifierContext]).get.asInstanceOf[IdentifierContext].getText,
      children.find(_.isInstanceOf[IdentifierContext]).get.getText,
      statement(children.find(_.isInstanceOf[StatementContext]).get.asInstanceOf[StatementContext])
    )
  }
  def clazz(ctx: ClassDeclarationContext): Clazz = {
    val children = ctx.children.asScala
    val extend:Option[String] = { //Returns identifier of extends if there is one
      val a = children.filter(_.isInstanceOf[IdentifierContext])
      if(a.length > 1)
        Option(a(1).asInstanceOf[IdentifierContext].getText)
      else
        None
    }
    Clazz(children.find(_.isInstanceOf[IdentifierContext]).asInstanceOf[IdentifierContext].getText,
      extend,
      varDecs(children.filter(_.isInstanceOf[VarDeclaration]).map(_.asInstanceOf[VarDeclarationContext])),
      methodDecs(children.filter(_.isInstanceOf[VarDeclaration]).map(_.asInstanceOf[MethodDeclarationContext]))
    )

  }

  def identifier(ctx:IdentifierContext): String = {
    ctx.getText
  }
  def statements(buffer:mutable.Buffer[StatementContext]): Array[Statement] = {
    buffer.map(statement).toArray
  }
  def statement(ctx:StatementContext): Statement = {
    val children = ctx.children.asScala
    children.toString()
    val a = children.head
    val st:Statement = a.getText match {
      case "{" =>
        val b = children.clone().slice(1,children.length - 1)
        BlockStatement(b.map(p => statement(p.asInstanceOf[StatementContext])).toArray)
      case "if" =>
        IfStatement(expression(children(2).asInstanceOf[ExpressionContext]),
          statement(children(4).asInstanceOf[StatementContext]),
          statement(children(6).asInstanceOf[StatementContext])
        )
      case "while" =>
        WhileStatement(expression(children(2).asInstanceOf[ExpressionContext]),
          statement(children(4).asInstanceOf[StatementContext])
        )
      case "println" =>
        PrintStatement(expression(children(2).asInstanceOf[ExpressionContext]))
      case _ =>
        if(children(1).getText == "=")
          Assignment(children.head.getText,expression(children(2).asInstanceOf[ExpressionContext]))
        else if(children(1).getText == "[") {
          ArrAssign(children.head.getText, expression(children(2).asInstanceOf[ExpressionContext]),
            expression(children(5).asInstanceOf[ExpressionContext])
          )
        }
        else
          throw new NoSuchElementException

    }
    st
  }
  def expressions(buffer:mutable.Buffer[ExpressionContext]): Array[Expression] = {
    buffer.map(expression).toArray
  }
  def expression(ctx: ExpressionContext): Expression = { //Todo If i have extra time make this not so crazy
    val children = ctx.children.asScala
    children.head match {
      case e:ExpressionContext => children(1).getText match {
        case "&&" => AndExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]))
        case "<" => LessExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]))
        case "*" => MulExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]))
        case "+" => SumExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]))
        case "-" => SubExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]))
        case "[" => ArrAccess(expression(e),expression(children(2).asInstanceOf[ExpressionContext]))
        case "." if children(2).getText=="length" => LengthExpression(expression(children(2).asInstanceOf[ExpressionContext]))
        case "." if children(2).isInstanceOf[IdentifierContext] => DotExpression(expression(e),children(2).getText,{
          val sub = children.tail.filter(_.isInstanceOf[ExpressionContext]).map(_.asInstanceOf[ExpressionContext])
          expressions(sub)
        })
      }
      case e:LambdaContext => lambda(e)
      case e:TerminalNodeImpl => e.symbol.getType match {
        case MinijavaParser.INTLIT => if(children.length ==2) IntLit(children(1).getText.toInt * (if(children.head.getText == "-") -1 else 1)) else IntLit(children(1).getText.toInt)
        case MinijavaParser.TRUE => BoolLit(true)
        case MinijavaParser.FALSE => BoolLit(false)
        case MinijavaParser.THIS => ThisExpression()
        case MinijavaParser.NEW if children(1).isInstanceOf[TerminalNodeImpl] => NewIntArrExpression(expression(children(3).asInstanceOf[ExpressionContext]))
        case MinijavaParser.NEW if children(1).isInstanceOf[IdentifierContext] => NewIdentExpression(children(1).getText)
        case MinijavaParser.NEG => NegateExpression(expression(children(1).asInstanceOf))
        case MinijavaParser.LPAREN => ParenExpression(expression(children(1).asInstanceOf))
      }
      case e:IdentifierContext => Ident(e.getText)
    }
  }
  def lambda(ctx: LambdaContext): Lambda = {
    val children = ctx.children.asScala
    children(2) match {
      case e:ExpressionContext => LambdaExpression(parameters(children.head.asInstanceOf[ParamContext]),
        expression(children(2).asInstanceOf)
      )
      case e:TerminalNodeImpl => LambdaBlock(parameters(children.head.asInstanceOf),
        varDecs(children.filter(_.isInstanceOf[VarDeclarationContext]).map(_.asInstanceOf)),
        statements(children.filter(_.isInstanceOf[StatementContext]).map(_.asInstanceOf[StatementContext])),
        ReturnExpression(children(6).asInstanceOf)
      )
    }
  }
  def varDecs(buffer: mutable.Buffer[VarDeclarationContext]): Array[VarDeclaration] = {
    buffer.map(varDec).toArray
  }
  def varDec(ctx:VarDeclarationContext): VarDeclaration = {
    val children = ctx.children.asScala
    VarDeclaration(tipe(children.head.asInstanceOf[TypeContext]),children(1).getText)
  }
  def tipe(ctx:TypeContext): Type = {
    val children = ctx.children.asScala
    children.head.getText match {
      case "int" => IntType
      case "boolean" => BoolType
      case "int[]" => IntArrType
      case a:String =>identType(a)
    }
  }
  def identType(ident:String): IdentType = {
    IdentType(ident)
  }
  def methodDecs(buffer: mutable.Buffer[MethodDeclarationContext]): Array[MethodDec] = {
    buffer.map(methodDec).toArray
  }
  def methodDec(ctx:MethodDeclarationContext): MethodDec = {
    val children = ctx.children.asScala
    MethodDec(tipe(children(1).asInstanceOf[TypeContext]),
      identifier(children(2).asInstanceOf[IdentifierContext]),
      parameters(children(3).asInstanceOf[ParamContext]),
      varDecs(children.filter(_.isInstanceOf[VarDeclarationContext]).map(_.asInstanceOf[VarDeclarationContext])),
      statements(children.filter(_.isInstanceOf[StatementContext]).map(_.asInstanceOf[StatementContext])),
      expression(children(8).asInstanceOf[ExpressionContext])
    )
  }
  def parameters(context: ParamContext): Parameters = {
    val children = context.children.asScala
    var params = Array[(Type,String)]()
    for(i <- children) {
      i match {
        case context1: TypeContext =>
          params = params :+ (tipe(context1), children(children.indexOf(i) + 1).getText)
        case _ =>
      }
    }
    Parameters(params)
  }
}
