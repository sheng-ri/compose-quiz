package cn.hellozjf.project.composequiz.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizKey(
  val chapterIndex: Int,
  val quizIndex: Int
)