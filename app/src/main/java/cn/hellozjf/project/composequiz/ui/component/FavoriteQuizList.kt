package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.util.LanguageConstant

/**
 * 收藏测试页面 收藏的问答列表
 */
@Composable
fun FavoriteQuizList(
  language: String,
  quizDTOList: List<QuizDTO>,
  getChapterDTOByIndex: suspend (String, Int) -> ChapterDTO?,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {
  val listState = rememberLazyListState()
  val questionExpandMap = remember { mutableStateMapOf<QuizKey, Boolean>() }

  LazyColumn(
    modifier = modifier,
    state = listState
  ) {
    if (quizDTOList.isNotEmpty()) {
      quizDTOList.forEachIndexed { index, quizDTO ->
        val quizKey = QuizKey(
          chapterIndex = quizDTO.chapterIndex,
          quizIndex = quizDTO.quizIndex
        )
        item(key = quizKey.toString()) {
          val expand = questionExpandMap[quizKey] ?: false
          val onExpandChange: (Boolean) -> Unit = {
            questionExpandMap[quizKey] = it
          }
          FavoriteQuiz(
            language = language,
            expanded = expand,
            onExpandedChange = onExpandChange,
            quizDTO = quizDTO,
            getChapterDTOByIndex = getChapterDTOByIndex,
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
  val getChapterDTOByIndex: suspend (String, Int) -> ChapterDTO? = { language, chapterIndex ->
    ChapterDTO(
      index = chapterIndex,
      fullTitle = "第${chapterIndex}章完整版标题",
      simpleTitle = "简化标题",
      simpleUrl = "http://xx.com/sim/${chapterIndex}",
      fullUrl = "http://xxx.com/full/${chapterIndex}"
    )
  }
  val quizList = run {
    val result = mutableListOf<QuizDTO>()
    for (i in 0 until 10) {
      val quizDTO = QuizDTO(
        chapterIndex = i,
        quizIndex = 1,
        question = "问题$i",
        correctOption = "问题${i}正确选项",
        wrongOption1 = "问题${i}错误选项1",
        wrongOption2 = "问题${i}错误选项2",
        wrongOption3 = "问题${i}错误选项3",
        explanation = "问题${i}的解释"
      )
      result.add(quizDTO)
    }
    result
  }
  FavoriteQuizList(
    language = LanguageConstant.ZH,
    quizDTOList = quizList,
    getChapterDTOByIndex = getChapterDTOByIndex,
    onNavigation = {},
  )
}