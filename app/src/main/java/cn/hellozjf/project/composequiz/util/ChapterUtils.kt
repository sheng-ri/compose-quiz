package cn.hellozjf.project.composequiz.util

import cn.hellozjf.project.composequiz.dto.ChapterDTO
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.openqa.selenium.WebDriver
import java.io.File
import java.io.FileReader
import java.io.IOException
import kotlin.text.toInt

/**
 * 章节工具
 */
class ChapterUtils {

  companion object {

    val defaultPdfFilePath =
      "D:\\hellozjf\\code\\gitee\\ComposeQuiz\\other\\book\\JetpackCompose1.8Essentials\\JetpackCompose1.8Essentials.pdf"
    val defaultCsvFilePath = "src/main/assets/${ChapterConstant.PATH_EN}"
    val defaultExcelFilePath = "output.xlsx"

    /**
     * 从 PDF 文件中获取简要章节信息
     * 其中简要章节信息里面没有 simpleTitle 和 fullUrl
     */
    fun getSimpleChapterDTOListFromPdfFile(
      file: File = File(defaultPdfFilePath),
    ): List<ChapterDTO> {
      return PdfUtils.getChapterDTOList(file)
    }

    /**
     * 从 PDF 文件中获取章节信息
     * 其中章节信息里面的 simpleTitle 和 fullUrl 需要通过 selenium 打开网页获取
     */
    fun getChapterDTOListFromPdfFile(
      driver: WebDriver,
      file: File = File(defaultPdfFilePath),
      timeoutSeconds: Long = 10L
    ): List<ChapterDTO> {
      val simpleChapterDTOList = PdfUtils.getChapterDTOList(file)
      val chapterInfoList = SeleniumUtils.fillChapterDTOList(
        chapterDTOList = simpleChapterDTOList,
        driver = driver,
        timeoutSeconds = timeoutSeconds
      )
      return chapterInfoList
    }

    /**
     * 从文件中读取章节信息
     */
    fun getChapterDTOListFromCsv(
      file: File = File(defaultCsvFilePath)
    ): List<ChapterDTO> {

      val result = mutableListOf<ChapterDTO>()

      try {
        FileReader(file).use { reader ->

          val format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .setHeader()
            .build()
          val csvParser = CSVParser(reader, format)

          for (record in csvParser) {
            val chapterIndex = record.get(ChapterConstant.INDEX).toInt()
            val fullTitle = record.get(ChapterConstant.FULL_TITLE)
            val simpleTitle = record.get(ChapterConstant.SIMPLE_TITLE)
            val simpleUrl = record.get(ChapterConstant.SIMPLE_URL)
            val actualUrl = record.get(ChapterConstant.FULL_URL)
            result.add(ChapterDTO(
              index = chapterIndex,
              fullTitle = fullTitle,
              simpleTitle = simpleTitle,
              simpleUrl = simpleUrl,
              fullUrl = actualUrl
            ))
          }
        }
      } catch (e: IOException) {
        e.printStackTrace()
      }

      return result.toList()
    }

    /**
     * 获取用于 Excel 或 CSV 上面的标题
     */
    fun getHeader(): List<String> {
      val header = listOf(
        ChapterConstant.INDEX,
        ChapterConstant.FULL_TITLE,
        ChapterConstant.SIMPLE_TITLE,
        ChapterConstant.SIMPLE_URL,
        ChapterConstant.FULL_URL
      )
      return header
    }
  }
}