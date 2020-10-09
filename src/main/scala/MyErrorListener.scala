import org.antlr.v4.runtime.{BaseErrorListener, RecognitionException, Recognizer}

import scala.collection.mutable.ArrayBuffer

class MyErrorListener extends BaseErrorListener {
  var errors:ArrayBuffer[String] = ArrayBuffer()
  override def syntaxError(recognizer: Recognizer[_, _], offendingSymbol: Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    var sourceName = recognizer.getInputStream.getSourceName
    if(!sourceName.isEmpty) {
      sourceName = String.format("%s:%d:%d: ",sourceName,line,charPositionInLine)
    }
    val str = sourceName + "line " + line + ":" + charPositionInLine + " " + msg
    errors += str
  }
}
