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
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.util.LanguageConstant

/**
 * 收藏测试页面 收藏的问答
 */
@Composable
fun FavoriteQuiz(
  language: String,
  expanded: Boolean,
  onExpandedChange: (Boolean) -> Unit,
  quizDTO: QuizDTO,
  getChapterDTOByIndex: suspend (String, Int) -> ChapterDTO?,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {

  var chapterDTO: ChapterDTO? by remember { mutableStateOf(null) }

  LaunchedEffect(key1 = quizDTO) {
    chapterDTO = getChapterDTOByIndex(language, quizDTO.chapterIndex)
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
        val correctOptionKey = OptionKey(
          chapterIndex = quizDTO.chapterIndex,
          quizIndex = quizDTO.quizIndex,
          optionIndex = 0
        )
        Column {
          FavoriteQuizOption(
            option = quizDTO.options[0],
            optionKey = correctOptionKey.copy(optionIndex = 0),
            selectedOptionKey = correctOptionKey,
            correctOptionKey = correctOptionKey
          )
          FavoriteQuizOption(
            option = quizDTO.options[1],
            optionKey = correctOptionKey.copy(optionIndex = 1),
            selectedOptionKey = correctOptionKey,
            correctOptionKey = correctOptionKey
          )
          FavoriteQuizOption(
            option = quizDTO.options[2],
            optionKey = correctOptionKey.copy(optionIndex = 2),
            selectedOptionKey = correctOptionKey,
            correctOptionKey = correctOptionKey
          )
          FavoriteQuizOption(
            option = quizDTO.options[3],
            optionKey = correctOptionKey.copy(optionIndex = 3),
            selectedOptionKey = correctOptionKey,
            correctOptionKey = correctOptionKey
          )
          Explanation(quizDTO.explanation)
          chapterDTO?.let {
            QuizFromChapter(
              chapterDTO = it
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
    options = listOf(
      "题目正确选项",
      "题目错误选项1",
      "题目错误选项2",
      "题目错误选项3",
    ),
    correctOptionIndex = 0,
    explanation = "题目的解释"
  )
  val getChapterDTOByIndex: suspend (String, Int) -> ChapterDTO? = { language, chapterIndex ->
    ChapterDTO(
      index = chapterIndex,
      fullTitle = "第0章完整版标题",
      simpleTitle = "简化标题",
      simpleUrl = "http://xx.com/sim/0",
      fullUrl = "http://xxx.com/full/0"
    )
  }
  FavoriteQuiz(
    language = LanguageConstant.ZH,
    expanded = expand,
    onExpandedChange = onExpandChange,
    quizDTO = quizDTO,
    getChapterDTOByIndex = getChapterDTOByIndex,
    onNavigation = {}
  )
}