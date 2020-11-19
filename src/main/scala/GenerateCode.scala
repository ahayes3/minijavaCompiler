import org.objectweb.asm.{ClassWriter, Label, Opcodes}

import scala.collection.immutable.HashMap
import scala.collection.mutable

object GenerateCode {
  val typeMap: Map[Type, String] = HashMap(IntType -> "I", BoolType -> "Z", ArrType(IntType) -> "[I",ArrType(StringType) -> "[java/lang/String")
  def apply(goal:Goal): Array[Byte] = {
    val cw = new ClassWriter(0)
    goal.classes.foreach(visitClass(_,cw))
    ???
  }
  def visitClass(clazz:Clazz,cw:ClassWriter): Unit = {
    cw.visit(Opcodes.V13,Opcodes.ACC_PUBLIC,clazz.ident,null,clazz.parent,null)
    clazz.varDecs.foreach(visitField(_,cw))
  }
  def visitField(field: VarDeclaration, cw: ClassWriter): Unit = {
    //val fw = cw.visitField(Opcodes.ACC_PUBLIC,field.ident,,)
  }
}
