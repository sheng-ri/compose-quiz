package cn.hellozjf.project.composequiz.dto

import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizZh

/**
 *
 */
data class QuizDTO(
  val chapterIndex: Int,
  val quizIndex: Int,
  val question: String,
  val correctOption: String,
  val wrongOption1: String,
  val wrongOption2: String,
  val wrongOption3: String,
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
}

fun QuizEn.toDTO() = QuizDTO(
  chapterIndex = chapterIndex,
  quizIndex = quizIndex,
  question = question,
  correctOption = correctOption,
  wrongOption1 = wrongOption1,
  wrongOption2 = wrongOption2,
  wrongOption3 = wrongOption3,
  explanation = explanation
)

fun QuizZh.toDTO() = QuizDTO(
  chapterIndex = chapterIndex,
  quizIndex = quizIndex,
  question = question,
  correctOption = correctOption,
  wrongOption1 = wrongOption1,
  wrongOption2 = wrongOption2,
  wrongOption3 = wrongOption3,
  explanation = explanation
)