package cn.hellozjf.project.composequiz.dto

/**
 * 一个不带 wrongAnswerCount 的 Quiz
 */
data class QuizDTO(
  val id: Int,
  val chapterIndex: Int,
  val question: String,
  val correctOption: String,
  val wrongOption1: String,
  val wrongOption2: String,
  val wrongOption3: String,
  val explanation: String,
  val favorite: Boolean,
  val favoriteTime: Long,
)