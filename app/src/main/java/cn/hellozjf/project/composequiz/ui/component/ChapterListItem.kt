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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 这是 章节测试 页面的章节列表项目 组件
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

@Preview
@Composable
fun ChapterListItemPreview() {
  val chapter = Chapter(
    index = 0,
    simpleTitle = "简单标题0",
    fullTitle = "完全标题0",
    simpleUrl = "http://xxx.com/sim/0",
    fullUrl = "http://xxx.com/full/0"
  )
  val findQuizByChapterIndex: suspend (Int) -> List<Quiz> = { chapterIndex ->
    val result = mutableListOf<Quiz>()
    for (i in 0 until 10) {
      val quiz = Quiz(
        chapterIndex = chapterIndex,
        quizIndex = i + 1,
        question = "章节${chapterIndex}问题${i}",
        correctOption = "问题${i}正确选项",
        wrongOption1 = "问题${i}错误选项1",
        wrongOption2 = "问题${i}错误选项2",
        wrongOption3 = "问题${i}错误选项3",
        explanation = "章节${chapterIndex}问题${i}解释"
      )
      result.add(quiz)
    }
    result.toList()
  }
  ChapterListItem(
    chapter = chapter,
    findQuizByChapterIndex = findQuizByChapterIndex,
    onNavigation = {}
  )
}