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
  // TODO quizList 最好改成 List<Pair<Int,Int>> 第一个是 chapterIndex，第二个是 quizIndex
  val quizList: List<Quiz>
): NavKey

@Serializable
data class QuizAnswerScreenKey(
  val title: String,
  // TODO quizList 最好改成 List<Pair<Int,Int>> 第一个是 chapterIndex，第二个是 quizIndex
  val quizList: List<Quiz>,
  val chooseOptionMap: Map<Int, String>,
  val quizOrderList: List<Int>,
  val optionOrderList: List<List<Int>>
): NavKey