package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import kotlinx.coroutines.flow.Flow

/**
 * 章节测验列表
 */
@Composable
fun ChapterQuizList(
  language: String,
  chapterIndex: Int,
  chapterSimpleTitle: String,
  findQuizDTOFlowByChapterIndex: (String, Int) -> Flow<List<QuizDTO>>,
  setFavorite: suspend (Int, Int, Boolean, Long) -> Unit,
  onNavigation: (NavKey) -> Unit
) {

  val quizDTOList by findQuizDTOFlowByChapterIndex(language, chapterIndex).collectAsState(listOf())
  val listState = rememberLazyListState()

  Column(
    modifier = Modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    LazyColumn(
      modifier = Modifier.weight(1f),
      state = listState
    ) {
      if (quizDTOList.isNotEmpty()) {
        quizDTOList.forEachIndexed { index, quizDTO ->
          item(key = quizDTO.getQuizKey()) {
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