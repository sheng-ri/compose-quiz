package cn.hellozjf.project.composequiz.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OptionKey(
  val chapterIndex: Int,
  val quizIndex: Int,
  val optionIndex: Int,
) : Parcelable {
  override fun toString(): String {
    return "${chapterIndex}_${quizIndex}_${optionIndex}"
  }
}