package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

/**
 * 这是收藏的问答
 */
@Composable
fun FavoriteQuiz(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  quiz: Quiz,
  chapterViewModel: ChapterViewModel,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {
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
          QuizFromChapter(
            chapterIndex = quiz.chapterIndex,
            chapterViewModel = chapterViewModel
          )
          WrongAnswerCount(quiz.wrongAnswerCount)
        }
      }
    }
  }
}
