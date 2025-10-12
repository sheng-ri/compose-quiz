package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
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
  chapterList: List<Chapter>,
  onItemClick: (Chapter) -> Unit,
  modifier: Modifier = Modifier
) {

  val lazyListState = rememberLazyListState()

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    state = lazyListState
  ) {
    chapterList.forEach { chapter ->
      item(key = chapter.id) {
        ChapterListItem(
          chapter = chapter,
          onItemClick = onItemClick
        )
      }
    }
  }
}