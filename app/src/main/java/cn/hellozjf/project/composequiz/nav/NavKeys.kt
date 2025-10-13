package cn.hellozjf.project.composequiz.nav

import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Quiz
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

/**
 * TODO 这里传 quizIdList 感觉会更好
 */
@Serializable
data class QuizScreenKey(
  val title: String,
  val quizList: List<Quiz>
): NavKey

@Serializable
data class QuizAnswerScreenKey(
  val title: String,
  val quizList: List<Quiz>,
  val chooseOptionMap: Map<Int, String>,
  val quizOrderList: List<Int>,
  val optionOrderList: List<List<Int>>
): NavKey