package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.QuizEn

private val TAG = "ChapterList"

/**
 * 这是章节测试页面 的章节列表 组件
 */
@Composable
fun ChapterList(
  chapterList: List<Chapter>,
  findQuizEnByChapterIndex: suspend (Int) -> List<QuizEn>,
  onNavigation: (NavKey) -> Unit,
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
          findQuizEnByChapterIndex = findQuizEnByChapterIndex,
          onNavigation = onNavigation
        )
      }
    }
  }
}

@Preview(
  showBackground = true
)
@Composable
fun ChapterListPreview() {
  val chapterList = run {
    val result = mutableListOf<Chapter>()
    for (i in 0 until 10) {
      result.add(
        run {
          val chapter = Chapter(
            index = i,
            simpleTitle = "简单标题$i",
            fullTitle = "完全标题$i",
            simpleUrl = "http://xxx.com/sim/$i",
            fullUrl = "http://xxx.com/full/$i"
          )
          chapter.id = i
          chapter
        }
      )
    }
    result
  }

  val findQuizEnByChapterIndex: suspend (Int) -> List<QuizEn> = { chapterIndex ->
    val result = mutableListOf<QuizEn>()
    for (i in 0 until 10) {
      val quizEn = QuizEn(
        chapterIndex = chapterIndex,
        quizIndex = i + 1,
        question = "章节${chapterIndex}问题${i}",
        correctOption = "问题${i}正确选项",
        wrongOption1 = "问题${i}错误选项1",
        wrongOption2 = "问题${i}错误选项2",
        wrongOption3 = "问题${i}错误选项3",
        explanation = "章节${chapterIndex}问题${i}解释"
      )
      result.add(quizEn)
    }
    result.toList()
  }

  ChapterList(
    chapterList = chapterList,
    findQuizEnByChapterIndex = findQuizEnByChapterIndex,
    onNavigation = {}
  )
}