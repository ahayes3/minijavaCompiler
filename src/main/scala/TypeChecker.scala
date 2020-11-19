import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class TypeCheckerError extends Error

case class VarStack(prev: VarStack, vars: HashMap[String, Type])

object TypeChecker {
  def typeEqual(t1: Type, t2: Type): Boolean = {
    if (t1 == t2 || t1 == SomeType || t2 == SomeType)
      true
    else
      false
  }

  def typeExist(t:Type,line:Int): Seq[String] = {
    var a = Seq[String]()
    t match {
      case i:IdentType if !hierarchy.containsClass(i.ident) && !hierarchy.containsLambda(i.ident) =>
        a :+= line + ":Error: type "+ i + " not found."
      case i:ArrType =>
        i.tipe match {
          case j: IdentType if !hierarchy.containsLambda(j.ident) && !hierarchy.containsClass(j.ident) =>
            a :+= line + ":Error: type "+ i + "[] not found."
          case _ =>
        }
      case _ =>
    }
    a
  }

  var hierarchy:Hierarchy = _
  var classes = new ArrayBuffer[String]()
  var avTypes = new ArrayBuffer[Type]()

  def apply(ast: Goal, hierarchy: Hierarchy): Seq[String] = {
    //inheritance = InheritanceNode("java/lang/Object", None, new ArrayBuffer[InheritanceNode]())
    this.hierarchy = hierarchy
    classes.clear()
    visit(ast, hierarchy.head)
  }

  def visit(node: Node,ctx:HNode): Seq[String] = {
    node match {
      case e: Goal => visit(e.main, null) :++ e.classes.flatMap(visit(_, null))
      case e: MainClass =>
        classes.addOne(e.ident)
        visit(e.statement, ctx.findMethod(e.ident,Seq[Type](ArrType(StringType))).get)

      case e: Clazz =>
        val newCtx = ctx.findClass(e.ident).get
       ( e.varDecs.flatMap(visit(_, newCtx)) :++ e.methods.flatMap(visit(_, newCtx))).to(mutable.ArrayBuffer)
      case e: MethodDec =>
        val newCtx = ctx.findMethod(e.ident,e.params.types).get
        var a = e.varDecs.flatMap(visit(_, newCtx))
        a :++= e.statements.flatMap(visit(_, newCtx))

        val out = typeExist(e.tipe,e.line)
        if(out.nonEmpty) {
          newCtx.rType = SomeType
          a :++= out
        }

        val (b, t1) = visitExp(e.ret, newCtx)
        a :++= b
        if (t1 != e.tipe && t1 != SomeType)
          a :++= (e.ret.line + ":Error: expected " + e.tipe + " found " + t1 + ".")
        a

      case e: VarDeclaration =>
        typeExist(e.tipe,e.line)
        var a = Seq()
        //todo check if name declared in current scope
        if(ctx.fields.keys.count(_ == e.ident) > 1)
          a :+= e.line + ":Error: duplicated identifier "+ e.ident+"."
        a

      case e: BlockStatement =>
        e.value.flatMap(visit(_, ctx))

      case e: Assignment =>
        var (b, tipe) = visitExp(e.value, ctx)
        var a = Seq[String]()
        a :++= b
        if(ctx.getVar(e.ident).isEmpty)
          a :+= (e.line + ":Error: Tried assigning to uninitialized identifier " + e.ident + ".")
        else
          throw new TypeCheckerError
        var t = ctx.getVar(e.ident).get

        val c1 = typeExist(t,e.line)
        val c2 = typeExist(tipe,e.value.line)
        if(c1.nonEmpty)
          t = SomeType
        if(c2.nonEmpty)
          tipe = SomeType

        a :++= (c1 :++ c2)

        e.value match {
          case p: LambdaExpression =>
            //todo check parameters and return for both of these
            t match {
              case i:IdentType =>
                val l = hierarchy.findLambda(i.ident)
                if(l.nonEmpty) {
                  //check param and return
                  if(l.get.method.parameters.types != p.params.types)
                    a :+= e.line + ":Error: mismatched parameters "+ l.get.method.parameters.types +" and "+ p.params.types + "."
                  if(!typeEqual(l.get.method.rType,tipe))
                    a :+= e.line + ":Error: mismatched return types " + l.get.method.rType + " and "+ tipe
                }
                else {
                  //type not found
                  a :+= e.line + ":Error: lambda " + i + " not found."
                }
              case _ => a :+= e.line + ":Error: can't assign lambda to "+ _ + "."
            }
          case p: LambdaBlock =>
            t match {
              case i:IdentType =>
                val l = hierarchy.findLambda(i.ident)
                if(l.nonEmpty) {
                  //check param and return
                  if(l.get.method.parameters.types != p.params.types)
                    a :+= e.line + ":Error: mismatched parameters "+ l.get.method.parameters.types +" and "+ p.params.types + "."
                  if(!typeEqual(l.get.method.rType,tipe))
                    a :+= e.line + ":Error: mismatched return types " + l.get.method.rType + " and "+ tipe
                }
                else {
                  //type not found
                  a :+= e.line + ":Error: lambda " + i + " not found."
                }
              case _ => a :+= e.line + ":Error: can't assign lambda to "+ _ + "."
            }
          case _ =>
            if(typeEqual(t,tipe))
              a = a :+ (e.line + ":Error: Mismatched types " + t + " and " + tipe + ".")
        }
        a

      case e: ArrAssign =>
        var a = Seq()
        val(b,t3) = visitExp(e.value,ctx)
        val(c,t2) = visitExp(e.offset,ctx)

        if(t2 != IntType)
          a :+= e.offset.line + ":Error: expected int found " + t2+"."

        a :++= (b :++ c)
        val t0 = ctx.getVar(e.ident)
        val t1 = if(t0.isEmpty) {
          a :+= e.line + ":Error: variable "+e.ident + " not found."
          SomeType
        }
        else if(!t0.get.isInstanceOf[ArrType]) {
          a :+= e.line + ":Error: mismatched types "+ t0.get + " and " + t3 +"."
        }
        else
          t0.get
        if(t1.asInstanceOf[ArrType].tipe !=t3 )
         a :+= e.line + ":Error: mismatched types "+ t1.asInstanceOf[ArrType].tipe + " and " + t3 + "."
        a

      case e: PrintStatement =>
        val (b, t1) = visitExp(e.value, ctx)
        if (t1 != IntType && t1 != SomeType)
          b :+ (e.line + ":Error: expected int found " + t1 + ".")
        else
          b

      case e: WhileStatement =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.condition, ctx)
        a = a :++ b
        if (t1 != BoolType)
          a = a :+ (e.line + ":Error: expected boolean found " + t1 + ".")
        visit(e.statement, ctx)
        a

      case e: IfStatement =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.condition, ctx)
        a = a :++ b
        if (t1 != BoolType)
          a = a :+ (e.line + ":Error: expected boolean found " + t1 + ".")
        a = a :++ visit(e.statement, ctx)
        a :++ visit(e.elseStatement, ctx)

//      case e:LambdaI =>
//        var a = Seq[String]()
//        classSymbols(e.ident).methods((e.ident,e.params))

      case _ => throw new NotImplementedError("Shouldn't be here")
    }
  }

  def visitExp(exp: Expression, ctx:HNode): (Seq[String], Type) = {
    exp match {
      case e: MathExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.left, ctx)
        val (c, t2) = visitExp(e.right, ctx)
        a = a :++ b :++ c
        val operation = e match {
          case _:SumExpression => "add"
          case _:SubExpression => "subtract"
          case _:MulExpression => "multiply"
        }
        if (t1 != IntType || t2 != IntType)
          a = a :+ (e.line + ":Error: Cannot " +operation+ " to " + t1 + " and " + t2 + ".")
        (a, IntType)
      case e:LessExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.left,ctx)
        val (c,t2) = visitExp(e.right,ctx)
        a = a :++ b :++ c
        if((t1 != IntType && t1 != SomeType) || (t2 != IntType && t2 != SomeType))
          a = a :+ (e.line + ":Error: Cannot apply < to " + t1 + " and " + t2 + ".")
        (a,BoolType)
      case e:AndExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.left,ctx)
        val (c,t2) = visitExp(e.right,ctx)
        a = a :++ b :++ c
        if((t1 != BoolType && t1 != SomeType) || (t2 != BoolType && t2 != SomeType))
          a = a :+ (e.line + ":Error: Cannot apply && to " + t1 + " and " + t2 + ".")
        (a,BoolType)
      case e: Ident =>
        var a = Seq()
        val t = ctx.getVar(e.ident)
        val t1 = if(t.isEmpty) {
          a :+= e.line +":Error: variable "+e.ident + " not found."
          SomeType
        }
        else
          t.get
        (a, t1)
      case _: IntLit =>
        (Seq(), IntType)
      case _: BoolLit =>
        (Seq(), BoolType)

      case e: ArrAccess =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.array, ctx)
        a = a :++ b
        val(c,t2) = visitExp(e.offset,ctx)
        a = a :++c
        if(t2 != IntType)
          a = a :+ e.line + ":Error: expected int found "+t2+"."

        val t0 = t1 match {
          case i:ArrType => i.tipe
          case _ => throw new TypeCheckerError
        }
        (a,t0)
      case e: DotExpression => //todo
        var a = Seq[String]()
        val (b, t1) = visitExp(e.left, ctx)
        a = a :++ b
        if (!t1.isInstanceOf[IdentType])
          a :+ (e.line + ":Error: expected class found " + t1 + ".")
        else if(!classSymbols.contains(t1.toString))
          a :+ (e.line + ":Error: class "+t1.toString+ " not found.")
        val typeArr = e.args.map(p => {
          val (b, t0) = visitExp(p, env)
          a = a :++ b
          t0
        })
        println(e.line)
        val outType = t1 match {
          case i: IdentType =>

            classSymbols(i.ident).getMethodType(e.funct, typeArr).getOrElse(throw new TypeCheckerError)
          case _ => SomeType
        }

        //val outType = if(t1.isInstanceOf[IdentType]) classSymbols.get(t1.asInstanceOf[IdentType].ident).get.getMethodType(e.funct,typeArr)
        (a, outType)

      case e: LengthExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.expression, ctx)
        a = a :++ b
        if (!t1.isInstanceOf[ArrType])
          a = a :+ (e.line + ":Error: expected array found " + t1 + ".")
        (a, IntType)
      case _: ThisExpression =>
        (Seq(), IdentType(ctx.getThis.ident))
      case e: NewIntArrExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.size, ctx)
        a = a :++ b
        if (t1 != IntType)
          a = a :+ (e.line + ":Error: expected int found " + t1 + ".")
        (a, ArrType(IntType))
      case e: NewIdentExpression =>
        if (hierarchy.containsClass(e.ident)) //hierarchy contains identifer
          (Seq(e.line + ":Error: class " + e.ident + " not found."), SomeType)
        else {
          (Seq(), IdentType(e.ident))
        }
      case e: NegateExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.expression, ctx)
        a = a :++ b
        if (t1 != IntType && t1 != BoolType)
          a = a :+ (e.line + ":Error: expected (int,boolean) found " + t1 + ".")
        (a, t1)
      case e: ParenExpression =>
        val a = Seq[String]()
        val (b, t1) = visitExp(e.expression, ctx)
        (a :++ b, t1)
      case e: ReturnExpression =>
        var a = Seq[String]()
        val (b, t1) = visitExp(e.expression, ctx)
        a = a :++ b
        (a, t1)
      case e: LambdaExpression =>
        val (b, t1) = visitExp(e.expression, ctx)
        (b, t1)
      case e: LambdaBlock =>
        var a = Seq[String]()
        val (b,t1) = visitExp(e.returnExp,)  //todo argument should be something based on what lambda can see
        a :++= b
        (a,t1)
      /*
        var a = e.vars.flatMap(p => visit(p, newEnv))
        a = a :++ e.statements.flatMap(p => visit(p, newEnv))
        val (b, t1) = visitExp(e.returnExp, newEnv)
        (a :++ b, t1)*/

      case _ => throw new NotImplementedError("Shouldn't be here")
    }
  }
}
