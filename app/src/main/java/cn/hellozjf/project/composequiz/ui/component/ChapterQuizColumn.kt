package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.util.LanguageConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

/**
 * 章节测验列表
 */
@Composable
fun ChapterQuizColumn(
  language: String,
  chapterIndex: Int,
  chapterSimpleTitle: String,
  findQuizDTOFlowByChapterIndex: (String, Int) -> Flow<List<QuizDTO>>,
  setFavorite: suspend (Int, Int, Boolean, Long) -> Unit,
  setLastTestChapterIndex: suspend (Int) -> Unit,
  onNavigation: (NavKey) -> Unit
) {

  val quizDTOList by findQuizDTOFlowByChapterIndex(language, chapterIndex).collectAsState(listOf())
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  Column(
    modifier = Modifier.padding(4.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    LazyColumn(
      modifier = Modifier.weight(1f),
      state = listState
    ) {
      if (quizDTOList.isNotEmpty()) {
        quizDTOList.forEachIndexed { index, quizDTO ->
          item(key = quizDTO.getQuizKey().toString()) {
            QuizAnswerItem(
              setFavorite = setFavorite,
              index = index,
              quizDTO = quizDTO,
              selectedOptionKey = null
            )
          }
        }
      }
    }

    Button(
      onClick = {
        coroutineScope.launch { setLastTestChapterIndex(chapterIndex) }
        onNavigation(
          QuizScreenKey(
            title = chapterSimpleTitle,
            quizKeyList = quizDTOList.map {
              QuizKey(
                chapterIndex = it.chapterIndex,
                quizIndex = it.quizIndex
              )
            }
          )
        )
      }
    ) {
      Text("开始测试")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ChapterQuizColumnPreview() {
  ChapterQuizColumn(
    language = LanguageConstant.ZH,
    chapterIndex = 0,
    chapterSimpleTitle = "第0章",
    findQuizDTOFlowByChapterIndex = { language, chapterIndex ->
      if (language == LanguageConstant.ZH) {
        flowOf(
          listOf(
            QuizDTO(
              chapterIndex = 0,
              quizIndex = 1,
              question = "问题0",
              options = listOf(
                "正确选项",
                "错误选项1",
                "错误选项2",
                "错误选项3"
              ),
              correctOptionIndex = 0,
              explanation = "问题0解释"
            ),
            QuizDTO(
              chapterIndex = 0,
              quizIndex = 2,
              question = "问题1",
              options = listOf(
                "正确选项",
                "错误选项1",
                "错误选项2",
                "错误选项3"
              ),
              correctOptionIndex = 0,
              explanation = "问题1解释"
            ),
            QuizDTO(
              chapterIndex = 0,
              quizIndex = 3,
              question = "问题2",
              options = listOf(
                "正确选项",
                "错误选项1",
                "错误选项2",
                "错误选项3"
              ),
              correctOptionIndex = 0,
              explanation = "问题2解释"
            )
          )
        )
      } else {
        flowOf()
      }
    },
    setFavorite = { chapterIndex: Int,
                    quizIndex: Int,
                    favorite: Boolean,
                    favoriteTime: Long ->
    },
    setLastTestChapterIndex = {},
    onNavigation = {}
  )
}