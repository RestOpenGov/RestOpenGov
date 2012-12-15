import scala.io.Source.fromFile

object Scrapper {

  def main(args: Array[String]) = {
    println(args(0))
    serieToCSV(args(0), args(1))
  }

  def serieToCSV(serie: String, anio: String): Unit = {

    val lines = fromFile("./" + serie + ".txt").mkString.lines.toList

    // separate cuentas and subcuentas from valores
    val (nums1, texts1) = lines.partition { isNum _ }

    // only keep first value, get rid of the rest of the values
    val nums2 = nums1.map ( _.split(" ")(0))

    // drop firt element: Total gasto
    val (nums, texts) = (nums2.tail, texts1.tail)

    // variable de corte de control
    var cuenta = ""
    var rows = List[Row]()

    for (index <- 0 to texts.size-1) {
      if (isLevel1(texts(index))) {
        cuenta = texts(index)
      } else {
        rows = rows :+ Row(anio, cuenta, texts(index), nums(index))
      }
    }

    val out = 
      "anio,cuenta,subcuenta,valor\n" +
      rows.map(_.toCSV).mkString("\n")

    write("./" + serie + ".csv", out)
    
  }

  case class Row(anio: String, cuenta: String, subcuenta: String, valor: String) {
    def toCSV: String = {
      """"%s","%s","%s","%s"""".format(anio, cuenta, subcuenta, valor)
    }
  }

  def isNum(value: String): Boolean = {
    value.matches("""[0-9\ ,\.]+""")
  }

  def isLevel1(value: String): Boolean = {
    value.matches("""\d - .*""")
  }

  def write(fileName: String, content: String, append: Boolean = false):Unit = {
    import java.io.FileWriter

    val w = new FileWriter(fileName, append)
    w.write(content)
    w.close
  }

}