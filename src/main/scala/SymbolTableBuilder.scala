import scala.collection.mutable

object SymbolTableBuilder {
  def apply(goal:Goal): mutable.HashMap[String,Environment] = {
    val table = new mutable.HashMap[String,Environment]()
    //table.put(goal.main.ident,buildMain(goal.main))
    for(i <- goal.classes) {
      table.put(i.ident,buildClass(i))
    }

    for(i <- goal.lambdas) {
      table.put(i.ident,buildLambdas(i))
    }
    table
  }

  def buildClass(clazz:Clazz): Environment = {
    val env = new Environment()
    clazz.varDecs.foreach(p => env.addVar(p.ident,p.tipe))
    clazz.methods.foreach(p => env.addMethod(p.ident,p.params,p.tipe))
    env
  }

  def buildLambdas(lambda:LambdaI): Environment = {
    val env = new Environment()
    env.methods.put((lambda.ident,lambda.params),lambda.mTipe)
    env
  }
}