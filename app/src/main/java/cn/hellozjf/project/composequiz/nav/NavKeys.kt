package cn.hellozjf.project.composequiz.nav

import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.dto.QuizKey
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenKey : NavKey

@Serializable
data class QuizScreenKey(
  val title: String,
  val quizKeyList: List<QuizKey>
) : NavKey

@Serializable
data class ChapterQuizScreenKey(
  val chapterIndex: Int,
  val chapterSimpleTitle: String
): NavKey

@Serializable
data class QuizAnswerScreenKey(
  val title: String,
  val quizKeyList: List<QuizKey>,
  val chooseOptionMap: Map<String, String>,
  val quizOrderList: List<Int>,
  val optionOrderList: List<List<Int>>
) : NavKey