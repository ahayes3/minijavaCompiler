import scala.collection.mutable

class Environment(val prev:Environment,val ident:String) { //ident of empty string means this isn't a class environment
  val vars:mutable.HashMap[String,Type] = new mutable.HashMap()
  val methods:mutable.HashMap[(String,Parameters),Type] = new mutable.HashMap()

  def this() = this(null,"")

  def getBottom: Environment = {
    if(prev == null)
      this
    else
      prev.getBottom
  }

  def addMethod(ident:String,params:Parameters,tipe:Type): Unit = {
    methods.put((ident,params), tipe)
  }
  def getMethodType(ident:String,types:Seq[Type]): Option[Type] = {
    val key = methods.keys.find(p => p._1 == ident && p._2.param.map(p => p._1) == types)
    if(key.nonEmpty)
      methods.get(key.get)
    else None
  }
  def addVar(ident:String,tipe:Type): Unit = {
    vars.put(ident, tipe)
  }
  def getVarType(ident:String):Type = {
    vars.getOrElse(ident,if(prev != null)
      prev.getVarType(ident)
    else
      throw new TypeCheckerError)
  }
}
