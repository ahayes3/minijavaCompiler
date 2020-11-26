import MinijavaParser._
import org.antlr.v4.runtime.tree.TerminalNodeImpl

import scala.collection.mutable
import scala.jdk.CollectionConverters.ListHasAsScala

object AstBuilder {


  def apply(ctx: GoalContext): Goal = {
    var children = ctx.children.asScala
    val mainclass = mainClass(children.head.asInstanceOf[MainClassContext])
    children = children.tail

    if(children.last.isInstanceOf[TerminalNodeImpl])
      children.remove(children.size - 1)

//    val classes = for(a <- children.filter(_.isInstanceOf[ClassDeclarationContext])) yield {
//      clazz(a.asInstanceOf[ClassDeclarationContext])
//    }
    val classes = for(a <- children.filter(_.isInstanceOf[ClassDeclarationContext])) yield {
      clazz(a.asInstanceOf[ClassDeclarationContext])
    }
    val lambdas = for(a<- children.filter(_.isInstanceOf[LambdaDeclarationContext])) yield {
      lambdaI(a.asInstanceOf[LambdaDeclarationContext])
    }
    Goal(mainclass, classes.toSeq,lambdas.toSeq,ctx.getStart.getLine)
  }
  def mainClass(ctx:MainClassContext): MainClass = {
    val children = ctx.children.asScala
    MainClass(children.find(_.isInstanceOf[IdentifierContext]).get.asInstanceOf[IdentifierContext].getText,
      children.findLast(_.isInstanceOf[IdentifierContext]).get.getText,
      statement(children.find(_.isInstanceOf[StatementContext]).get.asInstanceOf[StatementContext]),
      ctx.getStart.getLine
    )
  }
  def clazz(ctx: ClassDeclarationContext): Clazz = {
    val children = ctx.children.asScala
    val extend:String = { //Returns identifier of extends if there is one
      val a = children.filter(_.isInstanceOf[IdentifierContext])
      if(a.length > 1)
        a(1).asInstanceOf[IdentifierContext].getText
      else
        "java/lang/Object"
    }
    Clazz(children.find(_.isInstanceOf[IdentifierContext]).get.asInstanceOf[IdentifierContext].getText,
      extend,
      varDecs(children.filter(_.isInstanceOf[VarDeclarationContext]).map(_.asInstanceOf[VarDeclarationContext])),
      methodDecs(children.filter(_.isInstanceOf[MethodDeclarationContext]).map(_.asInstanceOf[MethodDeclarationContext])) ,
      ctx.getStart.getLine
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
        BlockStatement(b.map(p => statement(p.asInstanceOf[StatementContext])).toSeq,ctx.getStart.getLine)
      case "if" =>
        IfStatement(expression(children(2).asInstanceOf[ExpressionContext]),
          statement(children(4).asInstanceOf[StatementContext]),
          statement(children(6).asInstanceOf[StatementContext]),
          ctx.getStart.getLine
        )
      case "while" =>
        WhileStatement(expression(children(2).asInstanceOf[ExpressionContext]),
          statement(children(4).asInstanceOf[StatementContext]),
          ctx.getStart.getLine
        )
      case "System.out.println" =>
        PrintStatement(expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
      case _ =>
        if(children(1).getText == "=")
          Assignment(children.head.getText,expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        else if(children(1).getText == "[") {
          ArrAssign(children.head.getText, expression(children(2).asInstanceOf[ExpressionContext]),
            expression(children(5).asInstanceOf[ExpressionContext]),
            ctx.getStart.getLine
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
        case "&&" => AndExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case "<" => LessExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case "*" => MulExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case "+" => SumExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case "-" => SubExpression(expression(e), expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case "[" => ArrAccess(expression(e),expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case "." if children(2).getText=="length" => LengthExpression(expression(children(2).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case "." if children(2).isInstanceOf[IdentifierContext] => DotExpression(expression(e),children(2).getText,{
          val sub = children.tail.filter(_.isInstanceOf[ExpressionContext]).map(_.asInstanceOf[ExpressionContext])
          expressions(sub)
        },ctx.getStart.getLine)
      }
      case e:LambdaContext => lambda(e)
      case e:TerminalNodeImpl => e.symbol.getType match {
        case MinijavaParser.INTLIT => IntLit(e.getText.toInt,ctx.getStart.getLine)
        case MinijavaParser.TRUE => BoolLit(true,ctx.getStart.getLine)
        case MinijavaParser.FALSE => BoolLit(false,ctx.getStart.getLine)
        case MinijavaParser.THIS => ThisExpression(ctx.getStart.getLine)
        case MinijavaParser.NEW if children(1).isInstanceOf[TerminalNodeImpl] => NewIntArrExpression(expression(children(3).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case MinijavaParser.NEW if children(1).isInstanceOf[IdentifierContext] => NewIdentExpression(children(1).getText,ctx.getStart.getLine)
        case MinijavaParser.NEG => NegateExpression(expression(children(1).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
        case MinijavaParser.LPAREN => ParenExpression(expression(children(1).asInstanceOf[ExpressionContext]),ctx.getStart.getLine)
      }
      case e:IdentifierContext => Ident(e.getText,ctx.getStart.getLine)
    }
  }
  def lambda(ctx: LambdaContext): Lambda = {
    val children = ctx.children.asScala
    children(2) match {
      case _:ExpressionContext => LambdaExpression(parameters(children.head.asInstanceOf[ParamContext]),
        expression(children(2).asInstanceOf[ExpressionContext]),
        ctx.getStart.getLine
      )
      case _:TerminalNodeImpl => LambdaBlock(parameters(children.head.asInstanceOf[ParamContext]),
        varDecs(children.filter(_.isInstanceOf[VarDeclarationContext]).map(_.asInstanceOf[VarDeclarationContext])),
        statements(children.filter(_.isInstanceOf[StatementContext]).map(_.asInstanceOf[StatementContext])),
        ctx.getStart.getLine
      )
    }
  }
  def varDecs(buffer: mutable.Buffer[VarDeclarationContext]): Array[VarDeclaration] = {
    buffer.map(varDec).toArray
  }
  def varDec(ctx:VarDeclarationContext): VarDeclaration = {
    val children = ctx.children.asScala
    VarDeclaration(tipe(children.head.asInstanceOf[TypeContext]),children(1).getText,ctx.getStart.getLine)
  }
  def tipe(ctx:TypeContext): Type = {
    val children = ctx.children.asScala
    children.head.getText match {
      case "int" => IntType
      case "boolean" => BoolType
      case "int[]" => ArrType(IntType)
      case "String[]" => ArrType(StringType)
      case a:String =>IdentType(a)
    }
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
      expression(children.find(_.isInstanceOf[ExpressionContext]).get.asInstanceOf[ExpressionContext]),
      ctx.getStart.getLine
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
    Parameters(params,context.getStart.getLine)
  }
  def lambdaI(ctx:LambdaDeclarationContext):LambdaI = {
    val children = ctx.children.asScala
    val ident = identifier(children(1).asInstanceOf[IdentifierContext])
    val mTipe = tipe(children(3).asInstanceOf[TypeContext])
    val mIdent = identifier(children(4).asInstanceOf[IdentifierContext])
    val params = parameters(children(5).asInstanceOf[ParamContext])
    LambdaI(ident,mIdent,mTipe,params,ctx.getStart.getLine)
  }
}
