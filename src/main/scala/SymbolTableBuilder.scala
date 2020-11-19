import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

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
    TypeChecker.inheritance = TypeChecker.InheritanceNode("java/lang/Object",None,new ArrayBuffer[TypeChecker.InheritanceNode]())
    var classes:ArrayBuffer[Clazz] = ArrayBuffer.from(goal.classes.toArray[Clazz])
    while(classes.nonEmpty) {

      val canDo = classes.filter(p => TypeChecker.inheritance.find(p.parent).nonEmpty)
      for (i <- canDo){
        if(i.ident=="MyVisitor")
          println(TypeChecker.inheritance.find("Visitor"))
        val a = i
        val parent = TypeChecker.inheritance.find(a.parent)
        if (parent.nonEmpty) {
          TypeChecker.inheritance.addChild(TypeChecker.InheritanceNode(a.ident,parent,new ArrayBuffer[TypeChecker.InheritanceNode]()))
          classes -= a
        }
      }
    }

    table
  }

  def buildClass(clazz:Clazz): Environment = {
    val env = new Environment()
    clazz.varDecs.foreach(p => env.addVar(p.ident,p.tipe))
    clazz.methods.foreach(p => {
      env.addMethod(p.ident,p.params,p.tipe)})
    env
  }

  def buildLambdas(lambda:LambdaI): Environment = {
    val env = new Environment()
    env.methods.put((lambda.ident,lambda.params),lambda.mTipe)
    env
  }
}
