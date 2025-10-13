package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteTestCustomCount(
  customTestCount: Int,
  onCustomTestCountChange: (Int) -> Unit,
  minValue: Int,
  maxValue: Int
) {
  OutlinedTextField(
    value = customTestCount.toString(),
    onValueChange = { newCustomTestCountStr ->
      val filtered = newCustomTestCountStr.filter { it.isDigit() }
      if (filtered.isNotEmpty()) {
        val num = filtered.toInt()
        if (num in minValue..maxValue) {
          onCustomTestCountChange(num)
        } else if (num < minValue) {
          onCustomTestCountChange(minValue)
        } else {
          onCustomTestCountChange(maxValue)
        }
      } else {
        onCustomTestCountChange(minValue)
      }
    },
    modifier = Modifier.width(64.dp),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
  )
}

@Preview(
  showBackground = true
)
@Composable
fun FavoriteTestCustomCountPreview() {
  var customTestCount by remember { mutableStateOf(10) }
  val minValue = 0
  val maxValue = 100
  FavoriteTestCustomCount(
    customTestCount = customTestCount,
    onCustomTestCountChange = {
      customTestCount = it
    },
    minValue = minValue,
    maxValue = maxValue
  )
}