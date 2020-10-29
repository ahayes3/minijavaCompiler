import scala.collection.mutable

object SymbolTableBuilder {
  def apply(goal:Goal): mutable.HashMap[String,Environment] = {
    val table = new mutable.HashMap[String,Environment]()
    //table.put(goal.main.ident,buildMain(goal.main))
    for(i <- goal.classes) {
      table.put(i.ident,buildClass(i))
    }
    table
  }
//  def buildMain(main:MainClass): Environment = {
//    val env = new Environment()
//    main.
//  }
  def buildClass(clazz:Clazz): Environment = {
    val env = new Environment()
    clazz.varDecs.foreach(p => env.addVar(p.ident,p.tipe))
    clazz.methods.foreach(p => env.addMethod(p.ident,p.params,p.tipe))
    env
  }

}
