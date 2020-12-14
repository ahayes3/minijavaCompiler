import soot.{Body, BodyTransformer, SceneTransformer}

import java.util

class MyTransformer extends BodyTransformer{
  override def internalTransform(b: Body, phaseName: String, options: util.Map[String, String]): Unit = {
    b.getUnits.forEach(p => println(p))
  }
}
