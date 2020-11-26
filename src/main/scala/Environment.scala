//import scala.collection.mutable
//
//class Environment(val prev:Environment,val ident:String) { //ident of empty string means this isn't a class environment
//  val vars:mutable.HashMap[String,Type] = new mutable.HashMap()
//  val methods:mutable.HashMap[(String,Parameters),Type] = new mutable.HashMap()
//
//  def this() = this(null,"")
//
//  def getBottom: Environment = {
//    if(prev == null)
//      this
//    else
//      prev.getBottom
//  }
//
//  def addMethod(ident:String,params:Parameters,tipe:Type): Unit = {
//    methods.put((ident,params), tipe)
//  }
//  def getMethodType(ident:String,types:Seq[Type]): Option[Type] = {
//    val matchNames = methods.keys.filter(p => p._1 == ident)
//    matchNames.filter(p => p._2.param.length == types.length)
//
//    for(i <- matchNames) {
//      val mParams = i._2.param.map(p => p._1)
//      try {
//        for (j <- mParams.indices) {
//          mParams(j) match {
//            case e:IdentType =>
//              if(!types(j).isInstanceOf[IdentType] || !(TypeChecker.inheritance.find(types(j).asInstanceOf[IdentType].ident).get.isParent(e.ident)|| e == types(j).asInstanceOf[IdentType]))
//                throw new MethodNotFoundException
//            case _ =>
//              if(mParams(j) != types(j))
//                throw new MethodNotFoundException
//          }
//        }
//        return Some(methods(i))
//        //return type if you get this far
//      }
//      catch {
//        case e:MethodNotFoundException =>
//      }
//    }
//    None
//  }
////  def getMethodType(ident:String,types:Seq[Type]): Option[Type] = {
////    if(methods.keys.exists(_._1 == "accept"))
////      println(methods.keys.find(_._1 == "accept").get._2.param.map(p => p._1))
////    val key = methods.keys.find(p => p._1 == ident && {
////      val paramTipes = p._2.param.map(p => p._1)
////      var valid = true
////      for(i <- paramTipes.indices) {
////        paramTipes(i) match {
////          case e:IdentType =>
////            val tmp = TypeChecker.inheritance.find(e.ident)
////            if(!types(i).isInstanceOf[IdentType] || !tmp.get.isParent(types(i).asInstanceOf[IdentType].ident))
////              throw new MethodNotFoundException
////          case _ =>
////            if(paramTipes(i) != types(i))
////              throw new MethodNotFoundException
////        }
////      }
////    })
////    if(key.nonEmpty)
////      methods.get(key.get)
////    else None
////  }
//  def addVar(ident:String,tipe:Type): Unit = {
//    vars.put(ident, tipe)
//  }
//  def getVarType(ident:String):Option[Type] = {
//    if(vars.contains(ident))
//      Some(vars(ident))
//    else if(prev != null)
//      prev.getVarType(ident)
//    else
//      None
//  }
//}
