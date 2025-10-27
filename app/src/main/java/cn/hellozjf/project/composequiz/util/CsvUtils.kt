package cn.hellozjf.project.composequiz.util

import cn.hellozjf.project.composequiz.database.converter.Converters
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.QuizDTO
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import kotlin.text.toInt

class CsvUtils {
  companion object {

    /**
     * 将 header 的标题，以及 dataList 的数据，写到文件中
     */
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