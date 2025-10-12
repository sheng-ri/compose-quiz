package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.util.OrderConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

/**
 * 这是收藏的问答列表
 */
@Composable
fun FavoriteQuizList(
  selectText: String,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {
  val listState = rememberLazyListState()
  val questionExpandMap = remember { mutableStateMapOf<Int, Boolean>() }

  // TODO collectAsState 不知道是不是要改成 collectAsStateWithLifecycle
  val quizList by when (selectText) {
    OrderConstant.CHAPTER -> {
      chapterQuizViewModel.findByFavoriteOrderByChapterIndex().collectAsState(listOf())
    }

    OrderConstant.FAVORITE_TIME -> {
      chapterQuizViewModel.findByFavoriteOrderByFavoriteTime().collectAsState(listOf())
    }

    OrderConstant.WRONG_ANSWER_COUNT -> {
      chapterQuizViewModel.findByFavoriteOrderByWrongAnswerCount().collectAsState(listOf())
    }

    else -> {
      chapterQuizViewModel.findByFavoriteOrderByChapterIndex().collectAsState(listOf())
    }
  }

  LazyColumn(
    modifier = modifier,
    state = listState
  ) {
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
            chapterViewModel = chapterViewModel,
            onNavigation = onNavigation
          )
        }
      }
    }
  }
}