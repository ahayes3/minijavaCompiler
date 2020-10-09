import java.io.{File, FileNotFoundException}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream, Parser, Token}

import scala.io.Source

object Main {
  def main(args:Array[String]): Unit = {
   // val file = if(!args.isEmpty) Some(Source.fromFile(args(0))) else None
    val file = if(!args.isEmpty) Some(Source.fromFile("testFiles/LambdaTest.java")) else None
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

//    val tokens = mjLexer.getAllTokens
//    tokens.forEach(p => println(mjParser.getVocabulary.getSymbolicName(p.getType)))

    val tree = mjParser.goal()
    if(errorListener.errors.isEmpty)
      println("Parse successful")
    else
      errorListener.errors.foreach(Console.err.println)
  }
  def test(f :File): Boolean = {
    val file = Source.fromFile(f.getPath)
    val lines = file.getLines.mkString("\n")
    file.close()

    val chrStream = CharStreams.fromString(lines)
    val mjLexer = new MinijavaLexer(chrStream)
    val mjParser = new MinijavaParser(new CommonTokenStream(mjLexer))
    val errorListener = new MyErrorListener
    mjLexer.removeErrorListeners()
    mjLexer.addErrorListener(errorListener)
    mjParser.removeErrorListeners()
    mjParser.addErrorListener(errorListener)

    val tree = mjParser.goal()
    if(errorListener.errors.isEmpty) {
      println("Parse successful")
      true
    }
    else {
      errorListener.errors.foreach(Console.err.println)
      false
    }
  }
}
