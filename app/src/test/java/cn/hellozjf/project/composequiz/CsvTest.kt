package cn.hellozjf.project.composequiz

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.junit.Test
import java.io.FileWriter

class CsvTest {
  @Test
  fun test() {
    FileWriter("output.csv").use { writer ->
      CSVPrinter(writer, CSVFormat.DEFAULT).use { printer ->
        // 写入表头
        printer.printRecord("姓名", "年龄", "城市")

        // 写入数据
        printer.printRecord("张三", 25, "北京")
        printer.printRecord("李四", 30, "上海")
      }
      println("CSV 文件写入完成！")
    }
  }
}