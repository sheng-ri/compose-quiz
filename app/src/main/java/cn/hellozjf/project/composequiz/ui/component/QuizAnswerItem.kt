package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Arrangement
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
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizDTO

@Composable
fun QuizAnswerItem(
  setFavorite: suspend (Int, Int, Boolean, Long) -> Unit,
  index: Int,
  quizDTO: QuizDTO,
  selectedOptionKey: OptionKey?,
  modifier: Modifier = Modifier
) {
  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier = modifier
      .padding(4.dp)
      .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      QuestionAndFavoriteRow(
        index = index,
        quizDTO = quizDTO,
        setFavorite = setFavorite
      )
      val optionList = quizDTO.options
      val correctOptionKey = OptionKey(
        chapterIndex = quizDTO.chapterIndex,
        quizIndex = quizDTO.quizIndex,
        optionIndex = quizDTO.correctOptionIndex
      )
      for ((index, option) in optionList.withIndex()) {
        CheckboxRow(
          option = option,
          optionKey = OptionKey(
            chapterIndex = quizDTO.chapterIndex,
            quizIndex = quizDTO.quizIndex,
            optionIndex = index
          ),
          selectedOptionKey = selectedOptionKey,
          correctOptionKey = correctOptionKey,
          showColor = true,
          enabled = false
        )
      }
      Explanation(quizDTO.explanation)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun QuizAnswerItemPreview() {
  var quizDTO by remember {
    mutableStateOf(
      QuizDTO(
        chapterIndex = 0,
        quizIndex = 0,
        question = "问题0",
        options = listOf(
          "错误答案3",
          "错误答案1",
          "正确答案",
          "错误答案2",
        ),
        correctOptionIndex = 2,
        explanation = "问题0解释",
        favorite = false,
        favoriteTime = 0L
      )
    )
  }
  QuizAnswerItem(
    index = 0,
    quizDTO = quizDTO,
    selectedOptionKey = OptionKey(
      chapterIndex = 0,
      quizIndex = 0,
      optionIndex = 1
    ),
    setFavorite = { chapterIndex, quizIndex, favorite, favoriteTime ->
      quizDTO = quizDTO.copy(
        favorite = favorite,
        favoriteTime = favoriteTime
      )
    }
  )
}