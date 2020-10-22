import java.io.{File, FileNotFoundException}

import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream, Parser, Token}

import scala.io.Source

object Main {
  def main(args:Array[String]): Unit = {
    val tree = Parse("testFiles/parsing/LambdaTest.java")
    val ast = AstBuilder(tree)
    val typeErrs =

    visit(ast)

    //ParseTreeWalker.DEFAULT.walk(astBuilder, tree)
  }
  def test(p: File): Boolean = {
    val tree = Parse(p.getAbsolutePath)
    true
  }
  def visit(node:Node): Unit = {
    node match {
      case e:Goal => println(e.main)
        visit(e.main)
      case _ => println("not yet matched")
    }
  }
}
