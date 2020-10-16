import org.antlr.v4.runtime.ParserRuleContext

import scala.collection.mutable
class TypeChecker extends MinijavaParserBaseListener{
  val typeMap = new mutable.HashMap[String,Int]()
}
