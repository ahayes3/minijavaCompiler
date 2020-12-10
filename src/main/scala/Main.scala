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
    val (tree,parsed) = Parse(path)

    if(!parsed)
      System.exit(-1)

    val ast = AstBuilder(tree)

    val (hierarchy,hErr) = HierarchyBuilder(ast)
    if(hErr.nonEmpty) {
      hErr.foreach(println)
      System.exit(1)
    }

    val typeErrors = TypeChecker(ast,hierarchy)

    if(typeErrors.nonEmpty) {
      typeErrors.foreach(println(_))
      System.exit(1)
    }
    else
      println("Step 2 successful")

    //todo
    //GenerateCode(ast)
  }
}
