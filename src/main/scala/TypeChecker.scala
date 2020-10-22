import java.rmi.UnexpectedException

import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class TypeCheckerError extends Error

object TypeChecker {
  case class InheritanceNode(name:String,parent:Option[InheritanceNode],children:mutable.ArrayBuffer[InheritanceNode]) {
    def isParent(ident:String): Boolean = {
      if(parent.getOrElse(return false).name == ident)
        true
      else
        parent.get.isParent(ident)
    }
    def find(ident:String): Option[InheritanceNode] = {
      if(this.name == ident)
        Option(this)
      else if(this.children.nonEmpty)
        this.children.find(_.find(ident).nonEmpty)
      else
        None
    }
  }

  var inheritance:InheritanceNode = _
  var classes:mutable.ArrayBuffer[String] = _

  def apply(ast:Goal): Array[String] = {
    inheritance = InheritanceNode("Object",None,new ArrayBuffer[InheritanceNode]())
    classes.clear()
    visit(ast,Int.MinValue,null)

  }
  def visit(node:Node,scope:Int,env:Environment): Array[String] = {
    node match {
      case e:Goal => visit(e.main,scope,env) :++ e.classes.flatMap(visit(_,scope,env))
      case e:MainClass => env.addVar(e.strArgsIdent,scope, StringArrType)
        classes.addOne(e.ident)
        visit(e.statement,scope+1,Environment(e.ident,HashMap(),HashMap()))
      case e:Clazz => classes.addOne(e.ident)
        var newEnv = Environment(e.ident,HashMap(),HashMap())
        val b = visitMethods(e.methods,scope+1,newEnv)
        newEnv = b._2
        var a = b._1

        val c = visitVarDecs(e.varDecs,scope,env)
        a = a:++ c._1
        newEnv = c._2
//        a = a.appendedAll(for(i <- e.varDecs) yield {
//            val b = visit(i,scope,newEnv)
//            newEnv = newEnv.addVar(i.ident,scope,i.tipe)
//            b
//          })
//        e.varDecs.flatMap(visit(_,Environment(e.ident,HashMap(),HashMap())))
        if(classes.contains(e.ident))
          a = a :+ (e.line+":Duplicate class: "+e.ident)
        if(inheritance.find(e.parent.getOrElse("Object")).isEmpty) (a :+ (e.line + ":Error: Parent class" + e.parent.get +"does not exist.")) else a
      case e:MethodDec =>
        var newEnv = env.addMethod(e.ident,e.params)
        var a:Array[String] = for(i <- e.params.param) yield {
          if(newEnv.containsInScope(i._2,scope))
            return Array(e.line + ":Error: Duplicate local variable.")
          else
            newEnv = newEnv.addVar(i._2,scope, i._1)
          Array()
        }
        val b = visitVarDecs(e.varDecs,scope,env)
        a = a :++ b._1
        newEnv = b._2

        a :++ e.statements.flatMap(visit(_,scope,newEnv)) :++ visit(e.ret,scope, newEnv)
      case e:VarDeclaration =>
        if(env.containsInScope(e.ident,scope))
          Array(e.line + ":Error: Duplicate variable names.")
        else if(!env.containsType(e.tipe) && !((e.tipe.isInstanceOf[IdentType])&& classes.contains(e.tipe.asInstanceOf[IdentType].ident)))
          Array(e.line + ":Error: Type "+ e.tipe +" not found.")
        else
          Array[String]()
      case e:BlockStatement => e.value.flatMap(visit(_,scope,env))
      case e:Assignment =>null


      case _ => throw new NotImplementedError("Shouldn't be here")
    }
  }
  def visitMethods(methods:Array[MethodDec],scope:Int,env:Environment): (Array[String],Environment) = {
    var newEnv = env
    (for(i <- methods) yield { //Returns tuple of for yield errors and newEnv
      val b = visit(i,scope,newEnv)
      if(b.isEmpty)
        newEnv = newEnv.addMethod(i.ident,i.params)
      b
    },newEnv)
  }
  def visitVarDecs(varDecs:Array[VarDeclaration],scope:Int,env:Environment):(Array[String],Environment) = {
    var newEnv = env;
    (for(i <- varDecs) yield {
      val b = visit(i,scope,newEnv)
      if(b.isEmpty)
        newEnv = newEnv.addVar(i.ident,scope,i.tipe)
      b
    },newEnv)
  }
  def visitExpression(expression:Expression,scope:Int,env:Environment):Either[Array[String],Type] = {
    expression match {
      case e:IntLit => Right(IntType)
      case e:BoolLit => Right(BoolType)
      case e:NewIntArrExpression => Right(IntArrType)
      case e:NewIdentExpression => Right(IdentType(e.ident))
      case e:ThisExpression => Right(IdentType(env.clazz))
      case e:MathExpression =>
        var a:Array[String] = Array()
        val left = visitExpression(e.left,scope,env)
        val right = visitExpression(e.right,scope,env)
        if(left.isLeft)
          a = a :++ left.swap.getOrElse(Array())
        if(right.isLeft)
          a = a :++ right.swap.getOrElse(Array())
        if(a.nonEmpty)
          Left(a)
        else if(left.getOrElse(throw new TypeCheckerError) == right.getOrElse(throw new TypeCheckerError))
          Left(Array[String](e.line + ":Error: Mismatched types "+ left.getOrElse() + " and " + right.getOrElse()))
        else
          Right(left.getOrElse())
      case e:Ident =>
        if(env.containsInScope(e.ident,scope))
          Right(IdentType(e.ident))
        else
          Left(Array(e.line + ":Error: Variable"+ e.ident +" not found."))
      //todo rest of expression cases
    }

  }
}
