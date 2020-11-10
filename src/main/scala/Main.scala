import java.io.File
import java.nio.file.{Files, Path}

import scala.io.StdIn

object Main {
  def main(args:Array[String]): Unit = {
    val path = if(args.nonEmpty) args(0) else ""
    if(!Files.isRegularFile(Path.of(path))) {
      println("File not found.")
      System.exit(1)
    }
    val tree = Parse(path)
    val ast = AstBuilder(tree)
    val classSymbols = SymbolTableBuilder(ast)
    val typeErrors = TypeChecker(ast,classSymbols)
    if(typeErrors.isEmpty)
      println("No errors found")
    else
      typeErrors.foreach(println(_))
  }
}
