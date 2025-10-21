package cn.hellozjf.project.composequiz.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * TODO 一些 data class 可能既要 @Parcelize 又要 @Serializable，我不知道会不会有问题
 */
@Parcelize
@Serializable
data class QuizKey(
  val chapterIndex: Int,
  val quizIndex: Int
) : Parcelable {
  override fun toString(): String {
    return "${chapterIndex}_${quizIndex}"
  }
}