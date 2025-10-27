package cn.hellozjf.project.composequiz.dto

import cn.hellozjf.project.composequiz.database.converter.Converters
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizZh

/**
 *
 */
data class QuizDTO(
  // 这个是书中实际的章节号
  val chapterIndex: Int,
  // 这个是问题序号，从0开始
  val quizIndex: Int,
  val question: String,
  val options: List<String>,
  // 这个是选项序号，从0开始
  val correctOptionIndex: Int,
  val explanation: String,
  val favorite: Boolean = false,
  val favoriteTime: Long = 0L,
  val wrongAnswerCount: Int = 0
) {
  fun getQuizKey(): QuizKey {
    return QuizKey(
      chapterIndex = chapterIndex,
      quizIndex = quizIndex
    )
  }

  /**
   * 生成用于 Excel 或 CSV 上面的数据行
   */
  fun toDataRow(): List<String> {
    return listOf(
      chapterIndex.toString(),
      quizIndex.toString(),
      question,
      Converters().fromList(options),
      correctOptionIndex.toString(),
      explanation
    )
  }
}

fun QuizEn.toDTO() = QuizDTO(
  chapterIndex = chapterIndex,
  quizIndex = quizIndex,
  question = question,
  options = options,
  correctOptionIndex = correctOptionIndex,
  explanation = explanation
)

fun QuizZh.toDTO() = QuizDTO(
  chapterIndex = chapterIndex,
  quizIndex = quizIndex,
  question = question,
  options = options,
  correctOptionIndex = correctOptionIndex,
  explanation = explanation
)