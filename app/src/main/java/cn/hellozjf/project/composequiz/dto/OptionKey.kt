package cn.hellozjf.project.composequiz.dto

import kotlinx.serialization.Serializable

@Serializable
data class OptionKey(
  val chapterIndex: Int,
  val quizIndex: Int,
  val optionIndex: Int,
) {
  override fun toString(): String {
    return "${chapterIndex}_${quizIndex}_${optionIndex}"
  }
}