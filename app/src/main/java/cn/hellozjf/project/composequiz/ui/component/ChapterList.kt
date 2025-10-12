package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val TAG = "ChapterList"

/**
 * 这是所有章节列表 组件
 */
@Composable
fun ChapterList(
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {

  val chapterList by chapterViewModel.findAllOrderByIndex().collectAsState(listOf())
  val coroutineScope = rememberCoroutineScope()
  val listState = rememberLazyListState()

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    state = listState
  ) {
    chapterList.forEach { chapter ->
      item(key = chapter.id) {
        ChapterListItem(
          index = chapter.index,
          simpleTitle = chapter.simpleTitle,
          onItemClick = { index ->
            coroutineScope.launch {
              // 在 IO 线程执行数据库查询
              val quizList = withContext(Dispatchers.IO) {
                chapterQuizViewModel.findQuizByChapter(chapter.index)
              }
              onNavigation(
                QuizScreenKey(
                  title = chapter.simpleTitle,
                  quizList = quizList
                )
              )
            }
          }
        )
      }
    }
  }
}