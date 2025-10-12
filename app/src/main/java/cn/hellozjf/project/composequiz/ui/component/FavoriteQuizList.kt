package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.util.OrderConstant

/**
 * 这是收藏的问答列表
 */
@Composable
fun FavoriteQuizList(
  selectText: String,
  getChapterByIndex: suspend (Int) -> Chapter?,
  findByFavoriteOrderByChapterIndex: suspend () -> List<Quiz>,
  findByFavoriteOrderByFavoriteTime: suspend () -> List<Quiz>,
  findByFavoriteOrderByWrongAnswerCount: suspend () -> List<Quiz>,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {
  val listState = rememberLazyListState()
  val questionExpandMap = remember { mutableStateMapOf<Int, Boolean>() }
  val quizListState = remember { mutableStateOf(listOf<Quiz>()) }

  LaunchedEffect(key1 = selectText) {
    val quizList = when (selectText) {
      OrderConstant.CHAPTER -> findByFavoriteOrderByChapterIndex()
      OrderConstant.FAVORITE_TIME -> findByFavoriteOrderByFavoriteTime()
      OrderConstant.WRONG_ANSWER_COUNT -> findByFavoriteOrderByWrongAnswerCount()
      else -> findByFavoriteOrderByChapterIndex()
    }
    quizListState.value = quizList
  }

  LazyColumn(
    modifier = modifier,
    state = listState
  ) {
    val quizList = quizListState.value
    if (quizList.isNotEmpty()) {
      quizList.forEachIndexed { index, quiz ->
        item(key = quiz.id) {
          val expand = questionExpandMap[quiz.id] ?: false
          val onExpandChange: (Boolean) -> Unit = {
            questionExpandMap[quiz.id] = it
          }
          FavoriteQuiz(
            expand = expand,
            onExpandChange = onExpandChange,
            quiz = quiz,
            getChapterByIndex = getChapterByIndex,
            onNavigation = onNavigation
          )
        }
      }
    }
  }
}