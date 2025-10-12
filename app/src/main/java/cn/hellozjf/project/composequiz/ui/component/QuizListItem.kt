package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.database.entity.Quiz

/**
 * 问答列表项目
 */
@Composable
fun QuizListItem(
  index: Int,
  quiz: Quiz,
  selectOption: String,
  onSelectOptionChange: (String) -> Unit,
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
      // 题目
      Text(
        text = "${index + 1}. ${quiz.question}"
      )
      val options = listOf(
        quiz.correctOption,
        quiz.wrongOption1,
        quiz.wrongOption2,
        quiz.wrongOption3
      )
      // 可以进行的选项
      for (order in optionOrder) {
        RadioButtonAndText(
          option = options[order],
          selectOption = selectOption,
          onSelectOptionChange = onSelectOptionChange
        )
      }
    }
  }
}