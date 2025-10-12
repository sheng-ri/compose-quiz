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
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.Quiz

/**
 * 这是收藏的问答
 */
@Composable
fun FavoriteQuiz(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  quiz: Quiz,
  getChapterByIndex: suspend (Int) -> Chapter?,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {

  var chapter: Chapter? by remember { mutableStateOf(null) }

  LaunchedEffect(key1 = quiz) {
    chapter = getChapterByIndex(quiz.id)
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
        onExpandChange = onExpandChange,
        expand = expand,
        quiz = quiz,
        modifier = Modifier
      )
      if (expand) {
        // 显示这题的所有选项，正确选项，解释，打错次数
        Column {
          FavoriteQuizOption(
            option = quiz.correctOption,
            selectOption = quiz.correctOption,
            correctOption = quiz.correctOption
          )
          FavoriteQuizOption(
            option = quiz.wrongOption1,
            selectOption = quiz.correctOption,
            correctOption = quiz.correctOption
          )
          FavoriteQuizOption(
            option = quiz.wrongOption2,
            selectOption = quiz.correctOption,
            correctOption = quiz.correctOption
          )
          FavoriteQuizOption(
            option = quiz.wrongOption3,
            selectOption = quiz.correctOption,
            correctOption = quiz.correctOption
          )
          Explanation(quiz.explanation)
          chapter?.let {
            QuizFromChapter(
              chapter = it
            )
          }
          WrongAnswerCount(quiz.wrongAnswerCount)
        }
      }
    }
  }
}
