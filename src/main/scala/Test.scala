object Test {
  def main(args: Array[String]): Unit = {
    val a = Beans(1)
    val b = Beans(2)
    println(a==b)
  }
}
case class Beans(a:Int)
