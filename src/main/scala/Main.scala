import java.io.{File, FileNotFoundException}
import scala.collection.mutable

object Main {
  def main(args:Array[String]): Unit = {
    val tree = Parse("testFiles/parsing/BinaryTree.java")
    val ast = AstBuilder(tree)
    val classSymbols = SymbolTableBuilder(ast)
    //visit(ast,classSymbols)
    val typeErrors = TypeChecker(ast,classSymbols)
    if(typeErrors.isEmpty)
      println("No errors found")
    else
      typeErrors.foreach(println(_))
    //ParseTreeWalker.DEFAULT.walk(astBuilder, tree)
  }
  def test(p: File): Boolean = {
    val tree = Parse(p.getAbsolutePath)
    true
  }
}
