package cn.hellozjf.project.composequiz.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

//@Serializable
//data object HomeScreen : NavKey
//
//@Serializable
//data class WelcomeScreen(val name: String) : NavKey
//
//@Serializable
//data object ProfileScreen : NavKey

@Serializable
data object MainScreenKey: NavKey

@Serializable
data class QuizScreenKey(val chapterIndex: Int): NavKey

@Serializable
data class QuizAnswerScreenKey(
  val chapterIndex: Int,
  val chooseOptionMap: Map<Int, String>,
  val quizOrderList: List<Int>,
  val optionOrderList: List<List<Int>>
): NavKey