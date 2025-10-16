package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.dto.QuizDTO

/**
 * 收藏测试页面 收藏的问答
 */
@Composable
fun FavoriteQuiz(
  expanded: Boolean,
  onExpandedChange: (Boolean) -> Unit,
  quizDTO: QuizDTO,
  getChapterByIndex: suspend (Int) -> Chapter?,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {

  var chapter: Chapter? by remember { mutableStateOf(null) }

  LaunchedEffect(key1 = quizDTO) {
    chapter = getChapterByIndex(quizDTO.chapterIndex)
  }

  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier = modifier
      .padding(3.dp)
      .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {

    Column {
      FavoriteQuizQuestion(
        onExpandedChange = onExpandedChange,
        expanded = expanded,
        quizDTO = quizDTO,
        modifier = Modifier
      )
      if (expanded) {
        // 显示这题的所有选项，正确选项，解释，打错次数
        Column {
          FavoriteQuizOption(
            option = quizDTO.correctOption,
            selectedOption = quizDTO.correctOption,
            correctOption = quizDTO.correctOption
          )
          FavoriteQuizOption(
            option = quizDTO.wrongOption1,
            selectedOption = quizDTO.correctOption,
            correctOption = quizDTO.correctOption
          )
          FavoriteQuizOption(
            option = quizDTO.wrongOption2,
            selectedOption = quizDTO.correctOption,
            correctOption = quizDTO.correctOption
          )
          FavoriteQuizOption(
            option = quizDTO.wrongOption3,
            selectedOption = quizDTO.correctOption,
            correctOption = quizDTO.correctOption
          )
          Explanation(quizDTO.explanation)
          chapter?.let {
            QuizFromChapter(
              chapter = it
            )
          }
          WrongAnswerCount(quizDTO.wrongAnswerCount)
        }
      }
    }
  }
}

@Preview(
  showBackground = true
)
@Composable
fun FavoriteQuizPreview() {
  var expand by remember { mutableStateOf(false) }
  val onExpandChange: (Boolean) -> Unit = {
    expand = it
  }
  val quizDTO: QuizDTO = QuizDTO(
    chapterIndex = 0,
    quizIndex = 1,
    question = "第0章题目的标题",
    correctOption = "题目正确选项",
    wrongOption1 = "题目错误选项1",
    wrongOption2 = "题目错误选项2",
    wrongOption3 = "题目错误选项3",
    explanation = "题目的解释"
  )
  val getChapterByIndex: suspend (Int) -> Chapter? = { chapterIndex ->
    Chapter(
      index = chapterIndex,
      fullTitle = "第0章完整版标题",
      simpleTitle = "简化标题",
      simpleUrl = "http://xx.com/sim/0",
      fullUrl = "http://xxx.com/full/0"
    )
  }
  FavoriteQuiz(
    expanded = expand,
    onExpandedChange = onExpandChange,
    quizDTO = quizDTO,
    getChapterByIndex = getChapterByIndex,
    onNavigation = {}
  )
}