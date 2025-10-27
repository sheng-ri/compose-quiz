package cn.hellozjf.project.composequiz.util

import cn.hellozjf.project.composequiz.database.converter.Converters
import cn.hellozjf.project.composequiz.dto.QuizDTO
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.openqa.selenium.WebDriver
import java.io.File
import java.io.FileReader
import java.io.IOException
import kotlin.text.toInt

class QuizUtils {
  companion object {

    val defaultEnCsvFilePath = "src/main/assets/${QuizConstant.PATH_EN}"
    val defaultZhCsvFilePath = "src/main/assets/${QuizConstant.PATH_ZH}"

    /**
     * 从网络读取题目信息
     * 返回结果是一个 Map，key 是章节序号，value 是该章节下面的所有题目
     */
    fun getChapterQuizDTOListFromNetwork(
      driver: WebDriver,
      skipChapterIndexSet: Set<Int>
    ): Map<Int, List<QuizDTO>> {
      val result = mutableMapOf<Int, List<QuizDTO>>()
      val chapterDTOList = ChapterUtils.getChapterDTOListFromCsv()
      for (chapterDTO in chapterDTOList) {
        if (skipChapterIndexSet.contains(chapterDTO.index)) {
          continue
        }
        // 打开网页
        driver.get(chapterDTO.simpleUrl)
        // 获取题目
        val quizList = SeleniumUtils.getQuizList(
          driver = driver,
          chapterIndex = chapterDTO.index
        )
        result[chapterDTO.index] = quizList
      }
      return result
    }

    fun writeQuizDTOListToCsv(
      file: File = File(defaultEnCsvFilePath),
      quizDTOList: List<QuizDTO>
    ) {
      CsvUtils.writeToCsv(
        file = file,
        header = getHeader(),
        dataList = quizDTOList
          .map {
            it.toDataRow()
          }
      )
    }

    /**
     * 从文件中读取题目信息
     */
    fun getQuizDTOListFromCsv(
      file: File = File(defaultEnCsvFilePath)
    ): List<QuizDTO> {

      val result = mutableListOf<QuizDTO>()

      try {
        FileReader(file).use { reader ->

          val format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .setHeader()
            .build()
          val csvParser = CSVParser(reader, format)

          for (record in csvParser) {
            val chapterIndex = record.get(QuizConstant.CHAPTER_INDEX).toInt()
            val quizIndex = record.get(QuizConstant.QUIZ_INDEX).toInt()
            val question = record.get(QuizConstant.QUESTION)
            val options = record.get(QuizConstant.OPTIONS)
            val correctOptionIndex = record.get(QuizConstant.CORRECT_OPTION_INDEX).toInt()
            val explanation = record.get(QuizConstant.EXPLANATION)
            result.add(QuizDTO(
              chapterIndex = chapterIndex,
              quizIndex = quizIndex,
              question = question,
              options = Converters().fromString(options),
              correctOptionIndex = correctOptionIndex,
              explanation = explanation
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
        QuizConstant.CHAPTER_INDEX,
        QuizConstant.QUIZ_INDEX,
        QuizConstant.QUESTION,
        QuizConstant.OPTIONS,
        QuizConstant.CORRECT_OPTION_INDEX,
        QuizConstant.EXPLANATION,
      )
      return header
    }
  }
}