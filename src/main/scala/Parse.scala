import java.io.{File, FileNotFoundException}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

import scala.io.Source

object Parse {
  def apply(str:String): (MinijavaParser.GoalContext,Boolean) = {
    val file = Some(Source.fromFile(str))
    val lines = file.getOrElse(throw new FileNotFoundException()).getLines.mkString("\n")
    file.get.close()
    val chrStream = CharStreams.fromString(lines)
    val mjLexer = new MinijavaLexer(chrStream)
    val mjParser = new MinijavaParser(new CommonTokenStream(mjLexer))
    val errorListener = new MyErrorListener
    mjLexer.removeErrorListeners()
    mjLexer.addErrorListener(errorListener)
    mjParser.removeErrorListeners()
    mjParser.addErrorListener(errorListener)
    val tree = mjParser.goal()

    val success = if(errorListener.errors.isEmpty) {
      println("Parse successful")
      true
    } else {
      errorListener.errors.foreach(Console.err.println)
      false
    }
    (tree,success)
  }
}
