package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.QuizEn

/**
 * 收藏测试页面 收藏的问答列表
 */
@Composable
fun FavoriteQuizList(
  quizEnList: List<QuizEn>,
  getChapterByIndex: suspend (Int) -> Chapter?,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {
  val listState = rememberLazyListState()
  val questionExpandMap = remember { mutableStateMapOf<Int, Boolean>() }

  LazyColumn(
    modifier = modifier,
    state = listState
  ) {
    if (quizEnList.isNotEmpty()) {
      quizEnList.forEachIndexed { index, quiz ->
        item(key = quiz.id) {
          val expand = questionExpandMap[quiz.id] ?: false
          val onExpandChange: (Boolean) -> Unit = {
            questionExpandMap[quiz.id] = it
          }
          FavoriteQuiz(
            expanded = expand,
            onExpandedChange = onExpandChange,
            quizEn = quiz,
            getChapterByIndex = getChapterByIndex,
            onNavigation = onNavigation
          )
        }
      }
    }
  }
}

@Preview(
  showBackground = true
)
@Composable
fun FavoriteQuizListPreview() {
  val getChapterByIndex: suspend (Int) -> Chapter? = { chapterIndex ->
    Chapter(
      index = chapterIndex,
      fullTitle = "第${chapterIndex}章完整版标题",
      simpleTitle = "简化标题",
      simpleUrl = "http://xx.com/sim/${chapterIndex}",
      fullUrl = "http://xxx.com/full/${chapterIndex}"
    )
  }
  val quizList = run {
    val result = mutableListOf<QuizEn>()
    for (i in 0 until 10) {
      val quizEn = QuizEn(
        id = i,
        chapterIndex = i,
        quizIndex = 1,
        question = "问题$i",
        correctOption = "问题${i}正确选项",
        wrongOption1 = "问题${i}错误选项1",
        wrongOption2 = "问题${i}错误选项2",
        wrongOption3 = "问题${i}错误选项3",
        explanation = "问题${i}的解释"
      )
      result.add(quizEn)
    }
    result
  }
  FavoriteQuizList(
    quizEnList = quizList,
    getChapterByIndex = getChapterByIndex,
    onNavigation = {},
  )
}