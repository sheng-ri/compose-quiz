package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val TAG = "ChapterScreen"

/**
 * 这是所有章节标题的列表
 */
@Composable
fun ChapterScreen(
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

@Composable
fun ChapterListItem(
  index: Int,
  simpleTitle: String,
  onItemClick: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier = modifier
      .padding(3.dp)
      .fillMaxWidth()
      .clickable {
        onItemClick(index)
      },
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "第 $index 章",
        modifier = Modifier.width(75.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = simpleTitle,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(8.dp)
      )
    }
  }
}