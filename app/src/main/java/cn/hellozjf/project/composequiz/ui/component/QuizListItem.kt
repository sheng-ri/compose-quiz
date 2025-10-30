package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.BuildConfig
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import kotlinx.coroutines.launch

/**
 * 问答列表项目
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizListItem(
  index: Int,
  quizDTO: QuizDTO,
  selectedOptionKey: OptionKey?,
  onSelectedOptionKeyChange: (OptionKey) -> Unit,
  modifier: Modifier = Modifier
) {

  val coroutineScope = rememberCoroutineScope()
  val tooltipState = rememberTooltipState(isPersistent = false)

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
      modifier = Modifier.padding(8.dp)
    ) {
      // 题目
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        TooltipBox(
          positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
          tooltip = {
            // 这是气泡内显示的内容
            PlainTooltip {
              Text("${quizDTO.chapterIndex}-${quizDTO.quizIndex}")
            }
          },
          state = tooltipState,
        ) {
          Text(
            text = "${index + 1}. ${quizDTO.question}",
            modifier = Modifier
              .weight(1f)
              .clickable {
                if (BuildConfig.DEBUG) {
                  coroutineScope.launch {
                    tooltipState.show()
                  }
                }
              }
          )
        }
      }
      val options = quizDTO.options
      // 可以进行的选项
      options.forEachIndexed { index, option ->
        RadioButtonRow(
          option = option,
          optionKey = OptionKey(
            chapterIndex = quizDTO.chapterIndex,
            quizIndex = quizDTO.quizIndex,
            optionIndex = index
          ),
          selectedOptionKey = selectedOptionKey,
          onSelectedOptionKeyChange = onSelectedOptionKeyChange
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
  var selectOptionKey by remember {
    mutableStateOf(
      OptionKey(
        chapterIndex = 0,
        quizIndex = 0,
        optionIndex = 0
      )
    )
  }
  QuizListItem(
    index = 0,
    quizDTO = QuizDTO(
      chapterIndex = 0,
      quizIndex = 0,
      question = "问题0",
      options = listOf(
        "正确选项",
        "错误选项1",
        "错误选项2",
        "错误选项3",
      ),
      correctOptionIndex = 0,
      explanation = "问题0解释"
    ),
    selectedOptionKey = selectOptionKey,
    onSelectedOptionKeyChange = {
      selectOptionKey = it
    }
  )
}