package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 单选框和文本，答题时的选项
 */
@Composable
fun RadioButtonAndText(
  option: String,
  selectOption: String,
  onSelectOptionChange: (String) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelectOptionChange(option) },
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = selectOption == option,
      onClick = { onSelectOptionChange(option) }
    )
    Text(
      text = option,
      modifier = Modifier.padding(start = 8.dp)
    )
  }
}