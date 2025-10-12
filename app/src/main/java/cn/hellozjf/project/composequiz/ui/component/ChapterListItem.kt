package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 这是所有章节列表项目 组件
 */
@Composable
fun ChapterListItem(
  chapter: Chapter,
  findQuizByChapterIndex: suspend (Int) -> List<Quiz>,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {

  val coroutineScope = rememberCoroutineScope()

  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier = modifier
      .padding(3.dp)
      .fillMaxWidth()
      .clickable {
        coroutineScope.launch {
          // 在 IO 线程执行数据库查询
          val quizList = withContext(Dispatchers.IO) {
            findQuizByChapterIndex(chapter.index)
          }
          onNavigation(
            QuizScreenKey(
              title = chapter.simpleTitle,
              quizList = quizList
            )
          )
        }
      },
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "第 ${chapter.index} 章",
        modifier = Modifier.width(75.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = chapter.simpleTitle,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(8.dp)
      )
    }
  }
}