package cn.hellozjf.project.composequiz.nav

import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizKey
import kotlinx.serialization.Serializable


@Serializable
data object MainScreenKey : NavKey

@Serializable
data class QuizScreenKey(
  val titleEn: String,                        // 语言为英文时的标题
  val titleZh: String,                        // 语言为中文时的标题
  val quizKeyList: List<QuizKey>,             // 问题列表
  val onAnswerAllCorrect: suspend () -> Unit = {},    // 回答完全正确的回调，以便我往 punch 表里添加记录
) : NavKey

@Serializable
data class ChapterQuizScreenKey(
  val chapterIndex: Int,
  val chapterSimpleTitle: String
) : NavKey

@Serializable
data class QuizAnswerScreenKey(
  val titleEn: String,            // 语言为英文时的标题
  val titleZh: String,            // 语言为中文时的标题
  val quizKeyList: List<QuizKey>,
  val chooseOptionMap: Map<QuizKey, OptionKey>,
  val quizOrderList: List<Int>,
  val optionOrderList: List<List<Int>>,
  val onAnswerAllCorrect: suspend () -> Unit
) : NavKey

@Serializable
data object PunchScreenKey : NavKey

@Serializable
data object LaunchScreenKey : NavKey