package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.database.entity.QuizEn

/**
 * 问答列表项目
 */
@Composable
fun QuizListItem(
  index: Int,
  quizEn: QuizEn,
  selectedOption: String,
  onSelectedOptionChange: (String) -> Unit,
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
        text = "${index + 1}. ${quizEn.question}"
      )
      val options = listOf(
        quizEn.correctOption,
        quizEn.wrongOption1,
        quizEn.wrongOption2,
        quizEn.wrongOption3
      )
      // 可以进行的选项
      for (order in optionOrder) {
        RadioButtonAndText(
          option = options[order],
          selectedOption = selectedOption,
          onSelectedOptionChange = onSelectedOptionChange
        )
      }
    }
  }
}

@Preview(
  showBackground = true
)
@Composable
fun QuizListItemPreview() {
  var selectOption by remember { mutableStateOf("错误选项3") }
  QuizListItem(
    index = 0,
    quizEn = QuizEn(
      id = 0,
      chapterIndex = 0,
      quizIndex = 1,
      question = "问题0",
      correctOption = "正确选项",
      wrongOption1 = "错误选项1",
      wrongOption2 = "错误选项2",
      wrongOption3 = "错误选项3",
      explanation = "问题0解释"
    ),
    selectedOption = selectOption,
    onSelectedOptionChange = {
      selectOption = it
    },
    optionOrder = listOf(1, 0, 3, 2)
  )
}