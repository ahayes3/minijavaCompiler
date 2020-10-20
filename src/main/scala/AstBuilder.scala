import MinijavaParser.{ClassDeclarationContext, MainClassContext}
import org.antlr.v4.runtime.tree.{ParseTree, TerminalNodeImpl}

import scala.collection.mutable
import scala.jdk.CollectionConverters.ListHasAsScala

object AstBuilder {
  var head:Node = _

  def apply(ctx: MinijavaParser.GoalContext): Node = {
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
  def mainClass(ctx:MinijavaParser.MainClassContext): MainClass = {
    val children = ctx.children.asScala
    MainClass(children.find(_.isInstanceOf[MinijavaParser.IdentifierContext]).get.asInstanceOf[MinijavaParser.IdentifierContext].getText,
      children.find(_.isInstanceOf[MinijavaParser.IdentifierContext]).get.getText,
      statement(children.find(_.isInstanceOf[MinijavaParser.StatementContext]).get.asInstanceOf[MinijavaParser.StatementContext])
    )
  }
  def clazz(ctx: MinijavaParser.ClassDeclarationContext): Clazz = {
    val children = ctx.children.asScala
    val extend:Option[String] = { //Returns identifier of extends if there is one
      val a = children.filter(_.isInstanceOf[MinijavaParser.IdentifierContext])
      if(a.length > 1)
        Option(a(1).asInstanceOf[MinijavaParser.IdentifierContext].getText)
      else
        None
    }
    Clazz(children.find(_.isInstanceOf[MinijavaParser.IdentifierContext]).asInstanceOf[MinijavaParser.IdentifierContext].getText,
      extend,
      varDecs(children.filter(_.isInstanceOf[VarDeclaration]).map(_.asInstanceOf[MinijavaParser.VarDeclarationContext])),
      methodDecs(children.filter(_.isInstanceOf[VarDeclaration]).map(_.asInstanceOf[MinijavaParser.MethodDeclarationContext]))
    )

  }

  def identifier(ctx:MinijavaParser.IdentifierContext): String = {
    ctx.getText
  }
  def statement(ctx:MinijavaParser.StatementContext): Statement = {
    val children = ctx.children.asScala
    null//TODO
  }
  def varDecs(buffer: mutable.Buffer[MinijavaParser.VarDeclarationContext]): Array[VarDeclaration] = {
    null //TODO
  }
  def methodDecs(buffer: mutable.Buffer[MinijavaParser.MethodDeclarationContext]): Array[MethodDec] = {
    null//TODO
  }
}
