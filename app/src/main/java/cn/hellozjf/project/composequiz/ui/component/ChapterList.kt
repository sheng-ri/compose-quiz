package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.util.LanguageConstant

private val TAG = "ChapterList"

/**
 * 这是章节测试页面 的章节列表 组件
 */
@Composable
fun ChapterList(
  language: String,
  lastTestChapterIndex: Int?,
  chapterDTOList: List<ChapterDTO>,
  findQuizDTOByChapterIndex: suspend (String, Int) -> List<QuizDTO>,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {

  val lazyListState = rememberLazyListState()

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    state = lazyListState
  ) {
    chapterDTOList.forEach { chapterDTO ->
      item(key = chapterDTO.index) {
        ChapterListItem(
          language = language,
          lastTestChapterIndex = lastTestChapterIndex,
          chapterDTO = chapterDTO,
          findQuizDTOByChapterIndex = findQuizDTOByChapterIndex,
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
    val result = mutableListOf<ChapterDTO>()
    for (i in 0 until 10) {
      result.add(
        run {
          ChapterDTO(
            index = i,
            simpleTitle = "简单标题$i",
            fullTitle = "完全标题$i",
            simpleUrl = "http://xxx.com/sim/$i",
            fullUrl = "http://xxx.com/full/$i"
          )
        }
      )
    }
    result
  }

  val findQuizDTOByChapterIndex: suspend (String, Int) -> List<QuizDTO> =
    { language, chapterIndex ->
      val result = mutableListOf<QuizDTO>()
      for (i in 0 until 10) {
        val quizDTO = QuizDTO(
          chapterIndex = chapterIndex,
          quizIndex = i + 1,
          question = "章节${chapterIndex}问题${i}",
          options = listOf(
            "问题${i}正确选项",
            "问题${i}错误选项1",
            "问题${i}错误选项2",
            "问题${i}错误选项3",
          ),
          correctOptionIndex = 0,
          explanation = "章节${chapterIndex}问题${i}解释"
        )
        result.add(quizDTO)
      }
      result.toList()
    }

  ChapterList(
    language = LanguageConstant.ZH,
    lastTestChapterIndex = 0,
    chapterDTOList = chapterList,
    findQuizDTOByChapterIndex = findQuizDTOByChapterIndex,
    onNavigation = {}
  )
}