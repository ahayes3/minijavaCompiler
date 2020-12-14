import soot.{G, ModulePathSourceLocator, ModuleScene, PackManager, Scene, SootClass, Transform}
import soot.options.Options

import java.io.File
import java.util.Optional
import scala.jdk.CollectionConverters.{CollectionHasAsScala, MapHasAsScala}

object Optimizer {
  def apply(mainName:String): Unit = {
    soot.Main.main(Array[String]("-cp", "build/"+mainName ,"--app", "-p", "wjop.smb", "on", "-p", "wjop.si", "off", "-W", "-O", mainName, "-pp"))
  }
}
