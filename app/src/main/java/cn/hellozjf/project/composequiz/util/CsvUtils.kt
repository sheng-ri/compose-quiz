package cn.hellozjf.project.composequiz.util

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileWriter

class CsvUtils {
  companion object {
    fun writeToCsv(
      file: File,
      header: List<String>,
      dataList: List<List<String>>
    ) {
      FileWriter(file).use { writer ->
        CSVPrinter(writer, CSVFormat.DEFAULT).use { printer ->
          // 写入表头
          printer.printRecord(header)

          // 写入数据
          for (data in dataList) {
            printer.printRecord(data)
          }
        }
        println("CSV 文件写入完成！")
      }
    }
  }
}