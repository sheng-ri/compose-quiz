package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.dto.OptionKey

/**
 * 单选框和文本，答题时的选项
 */
@Composable
fun CheckboxRow(
  option: String,
  optionKey: OptionKey,
  selectedOptionKey: OptionKey?,
  onSelectedOptionKeyChange: (OptionKey) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .clickable { onSelectedOptionKeyChange(optionKey) },
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.Start
  ) {
    Checkbox(
      checked = selectedOptionKey == optionKey,
      onCheckedChange = null
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = option
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
fun CheckboxRowPreview() {
  val optionKey = OptionKey(
    chapterIndex = 0,
    quizIndex = 0,
    optionIndex = 0
  )
  var selectOptionKey: OptionKey? by remember {
    mutableStateOf(null)
  }
  CheckboxRow(
    option = "错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案",
    optionKey = optionKey,
    selectedOptionKey = selectOptionKey,
    onSelectedOptionKeyChange = {
      selectOptionKey = if (selectOptionKey != it) {
        it
      } else {
        null
      }
    }
  )
}