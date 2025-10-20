package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
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
fun RadioButtonAndText(
  option: String,
  optionKey: OptionKey,
  selectedOptionKey: OptionKey?,
  onSelectedOptionKeyChange: (OptionKey) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelectedOptionKeyChange(optionKey) },
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = selectedOptionKey == optionKey,
      onClick = { onSelectedOptionKeyChange(optionKey) }
    )
    Text(
      text = option,
      modifier = Modifier.padding(start = 8.dp)
    )
  }
}

@Preview(
  showBackground = true,
  name = "未选中单选框"
)
@Composable
fun RadioButtonAndTextPreview1() {
  var selectOption by remember { mutableStateOf(OptionKey(
    chapterIndex = 1,
    quizIndex = 1,
    optionIndex = 0,
  )) }
  RadioButtonAndText(
    option = "正确答案",
    optionKey = selectOption,
    selectedOptionKey = selectOption,
    onSelectedOptionKeyChange = {
      selectOption = it
    }
  )
}

@Preview(
  showBackground = true,
  name = "选中单选框"
)
@Composable
fun RadioButtonAndTextPreview2() {
  var selectOptionKey by remember { mutableStateOf(OptionKey(
    chapterIndex = 1,
    quizIndex = 1,
    optionIndex = 0
  )) }
  RadioButtonAndText(
    option = "正确答案",
    optionKey = selectOptionKey,
    selectedOptionKey = selectOptionKey,
    onSelectedOptionKeyChange = {
      selectOptionKey = it
    }
  )
}