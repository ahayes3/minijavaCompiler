import java.io.{File, FileNotFoundException}

import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream, Parser, Token}

import scala.io.Source

object Main {
  def main(args:Array[String]): Unit = {
    val tree = Parse("testFiles/parsing/LambdaTest.java")
    val ast = AstBuilder(tree)

    //ParseTreeWalker.DEFAULT.walk(astBuilder, tree)
  }
}
