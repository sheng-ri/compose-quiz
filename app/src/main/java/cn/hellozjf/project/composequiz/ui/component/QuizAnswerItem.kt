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
import cn.hellozjf.project.composequiz.database.entity.Quiz

@Composable
fun QuizAnswerItem(
  setFavorite: suspend (Int, Boolean, Long) -> Unit,
  index: Int,
  quiz: Quiz,
  selectOption: String,
  optionOrder: List<Int>,
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
      QuestionAndFavoriteRow(
        index = index,
        quiz = quiz,
        setFavorite = setFavorite
      )
      val optionList =
        listOf(quiz.correctOption, quiz.wrongOption1, quiz.wrongOption2, quiz.wrongOption3)
      for (order in optionOrder) {
        FavoriteQuizOption(
          option = optionList[order],
          selectOption = selectOption,
          correctOption = quiz.correctOption
        )
      }
      Explanation(quiz.explanation)
    }
  }
}