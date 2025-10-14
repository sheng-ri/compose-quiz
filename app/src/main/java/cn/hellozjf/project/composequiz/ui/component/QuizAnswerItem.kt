package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.database.entity.Quiz

@Composable
fun QuizAnswerItem(
  setFavorite: suspend (Int, Boolean, Long) -> Unit,
  index: Int,
  quiz: Quiz,
  selectedOption: String,
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
          selectedOption = selectedOption,
          correctOption = quiz.correctOption
        )
      }
      Explanation(quiz.explanation)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun QuizAnswerItemPreview() {
  var quiz by remember {
    mutableStateOf(
      Quiz(
        id = 0,
        chapterIndex = 0,
        quizIndex = 1,
        question = "问题0",
        correctOption = "正确答案",
        wrongOption1 = "错误答案1",
        wrongOption2 = "错误答案2",
        wrongOption3 = "错误答案3",
        explanation = "问题0解释",
        favorite = false,
        favoriteTime = 0L
      )
    )
  }
  QuizAnswerItem(
    index = 0,
    quiz = quiz,
    selectedOption = "错误答案1",
    optionOrder = listOf(3, 1, 2, 0),
    setFavorite = { index, favorite, favoriteTime ->
      if (index == 0) {
        quiz = quiz.copy(
          favorite = favorite,
          favoriteTime = favoriteTime
        )
      }
    }
  )
}