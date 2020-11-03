import java.io.File

object Main {
  def main(args:Array[String]): Unit = {
    val tree = Parse("testFiles/parsing/LambdaTest.java")
    val ast = AstBuilder(tree)
    val classSymbols = SymbolTableBuilder(ast)
    val typeErrors = TypeChecker(ast,classSymbols)
    if(typeErrors.isEmpty)
      println("No errors found")
    else
      typeErrors.foreach(println(_))
  }
  def test(p: File): Boolean = {
    val tree = Parse(p.getAbsolutePath)
    true
  }
}