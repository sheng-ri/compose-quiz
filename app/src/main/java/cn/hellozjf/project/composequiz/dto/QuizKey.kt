package cn.hellozjf.project.composequiz.dto

import kotlinx.serialization.Serializable

/**
 * TODO 一些 data class 可能既要 @Parcelize 又要 @Serializable，我不知道会不会有问题
 */
@Serializable
data class QuizKey(
  val chapterIndex: Int,
  val quizIndex: Int
) {
  override fun toString(): String {
    return "${chapterIndex}_${quizIndex}"
  }
}