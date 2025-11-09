package cn.hellozjf.project.composequiz.util

import cn.hellozjf.project.composequiz.database.converter.Converters
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.openqa.selenium.WebDriver
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.Reader

class QuizUtils {
  companion object {

    val defaultExtCsvFilePath = "src/main/assets/${AssetUtils.QUIZ_EXT_CSV}"
    val defaultEnCsvFilePath = "src/main/assets/${AssetUtils.QUIZ_EN_CSV}"
    val defaultZhCsvFilePath = "src/main/assets/${AssetUtils.QUIZ_ZH_CSV}"

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

    fun writeQuizDTOListToExtCsv(
      file: File = File(defaultExtCsvFilePath),
      quizDTOList: List<QuizDTO>
    ) {
      CsvUtils.writeToCsv(
        file = file,
        header = getExtHeader(),
        dataList = quizDTOList
          .map {
            it.toExtDataRow()
          }
      )
    }

    fun getQuizDTOListFromCsv(
      reader: Reader,
      quizKeyDescriptionMap: Map<QuizKey, String> = mutableMapOf()
    ): List<QuizDTO> {
      val result = mutableListOf<QuizDTO>()

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
        result.add(
          QuizDTO(
            chapterIndex = chapterIndex,
            quizIndex = quizIndex,
            question = question,
            description = quizKeyDescriptionMap[QuizKey(chapterIndex, quizIndex)] ?: "",
            options = Converters().fromString(options),
            correctOptionIndex = correctOptionIndex,
            explanation = explanation
          )
        )
      }

      return result.toList()
    }

    /**
     * 从文件中读取题目信息
     */
    fun getQuizDTOListFromCsv(
      file: File = File(defaultEnCsvFilePath)
    ): List<QuizDTO> {
      try {
        FileReader(file).use { reader ->
          return getQuizDTOListFromCsv(reader)
        }
      } catch (e: IOException) {
        e.printStackTrace()
        return listOf()
      }
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

    /**
     * 获取用于 Excel 或 CSV 上面的 Ext 标题
     */
    fun getExtHeader(): List<String> {
      val header = listOf(
        QuizConstant.CHAPTER_INDEX,
        QuizConstant.QUIZ_INDEX,
        QuizConstant.DESCRIPTION
      )
      return header
    }

    /**
     * 将 oldQuizDTOList 的题目按 quizOrder 重排序，题目的选项按 optionOrders 重排序
     */
    fun reorder(
      oldQuizDTOList: List<QuizDTO>,
      quizOrder: List<Int>,
      optionOrders: List<List<Int>>
    ): List<QuizDTO> {
      if (oldQuizDTOList.isEmpty()) {
        return listOf()
      }
      val map = mutableMapOf<QuizDTO, QuizDTO>()
      for ((index, quizDTO) in oldQuizDTOList.withIndex()) {
        val newQuizDTO = reorder(quizDTO, optionOrders[index])
        map.put(quizDTO, newQuizDTO)
      }
      val quizList = mutableListOf<QuizDTO>()
      for (order in quizOrder) {
        quizList.add(map[oldQuizDTOList[order]]!!)
      }
      return quizList.toList()
    }

    /**
     * 将 oldQuizDTO 的选项按 optionOrder 重排序
     */
    private fun reorder(
      oldQuiz: QuizDTO,
      optionOrder: List<Int>
    ): QuizDTO {
      val options = mutableListOf<String>()
      for (order in optionOrder) {
        options.add(oldQuiz.options[order])
      }
      val correctOptionIndex = optionOrder.indexOf(oldQuiz.correctOptionIndex)
      return oldQuiz.copy(
        options = options,
        correctOptionIndex = correctOptionIndex
      )
    }
  }
}