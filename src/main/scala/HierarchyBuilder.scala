import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
class HierarchyBuilderError extends Exception()
object HierarchyBuilder {
  def apply(goal:Goal): (Hierarchy,mutable.ArrayBuffer[String]) = {
    val err = mutable.ArrayBuffer[String]()
    val classes:mutable.Buffer[Clazz] = goal.classes.toBuffer.clone()
    val hier = new Hierarchy
    for(i <- classes) {
      if(classes.count(_.ident == i.ident)> 1) {
        err += classes.findLast(_.ident == i.ident).get.line + ":Error: duplicate class "+i.ident +"."
      }
    }
    while(classes.nonEmpty) {
      val canDo = classes.filter(p => hier.containsClass(p.parent))
      classes --= canDo
      if(canDo.isEmpty) {
        classes.foreach(p => (err += (p.line + ":Error: Parent "+ p.parent+" not found.")))
        throw new HierarchyBuilderError
      }
      canDo.foreach(p => hier.addClass(p))
    }
    goal.lambdas.foreach(hier.addLambda)

    (hier,err)
  }
}
