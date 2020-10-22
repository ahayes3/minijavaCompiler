import scala.collection.immutable.HashMap

case class Environment(clazz:String,vars: HashMap[(String,Int),Type],methods:HashMap[String,Parameters]) {
//  val vars = new HashMap[String,Type]()
//  val methods = new HashMap[String,Parameters]()

  def getVarType(str:String,scope:Int): Option[Type] = {
    var a = scope
    while(a > Int.MinValue) {
      if(vars.get(str,scope).nonEmpty)
        return vars.get(str,scope)
      a= a-1
    }
    None
  }
  private def containsVar(str:String,i:Int): Boolean = {
    vars.contains(str,i)
  }
  def addVar(str:String,i:Int,tipe: Type): Environment = {
    Environment(clazz,vars + ((str,i )-> tipe),methods)
  }
  def containsMethod(str:String, params:Parameters): Boolean = {
    if(!methods.contains(str))
      false
    else if(methods.contains(str) && paramsEqual(params,methods(str)))
      true
    else
      false
  }
  def addMethod(str:String,params:Parameters): Environment = {
    Environment(clazz,vars,methods+(str -> params))
  }
  def containsType(t:Type): Boolean = {
    vars.values.toArray.contains(t)
  }
  def containsInScope(str:String,i:Int):Boolean = {
    getVarsScope(i).map(_._1).contains(str)
  }
  def getVarsScope(i:Int): Array[(String,Int)] = {
    vars.keySet.filter(_._2==i).toArray
  }
  private def paramsEqual(p1:Parameters,p2:Parameters): Boolean = {
    if(p1.param.length != p2.param.length)
      false
    else {
      for(i <- p1.param.indices) {
        if(p1.param(i)._1 != p2.param(i)._1)
          return true
      }
      false
    }
  }
}
