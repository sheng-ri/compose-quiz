package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.hellozjf.project.composequiz.util.OrderMethodConstant

/**
 * TODO 两个下拉框合并成一个下拉框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderMethodDropdown(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  items: List<String>,
  selectItem: String,
  onSelectItemChange: (String) -> Unit,
) {
  ExposedDropdownMenuBox(
    expanded = expand,
    onExpandedChange = onExpandChange
  ) {
    TextField(
      value = selectItem,
      onValueChange = {},
      modifier = Modifier
        .fillMaxWidth()
        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
      readOnly = true,
      trailingIcon = {
        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expand)
      }
    )

    ExposedDropdownMenu(
      expanded = expand,
      onDismissRequest = { onExpandChange(false) }
    ) {
      items.forEach { item ->
        DropdownMenuItem(
          text = { Text(text = item) },
          onClick = {
            onSelectItemChange(item)
            onExpandChange(false)
          }
        )
      }
    }
  }
}

@Preview(
  showBackground = true
)
@Composable
fun OrderMethodDropdownPreview() {
  var expand by remember { mutableStateOf(false) }
  val onExpandChange: (Boolean) -> Unit = {
    expand = it
  }
  val items: List<String> = listOf(
    OrderMethodConstant.CHAPTER,
    OrderMethodConstant.FAVORITE_TIME,
    OrderMethodConstant.WRONG_ANSWER_COUNT,
  )
  var selectItem by remember { mutableStateOf(OrderMethodConstant.CHAPTER) }
  val onSelectItemChange: (String) -> Unit = {
    selectItem = it
  }
  OrderMethodDropdown(
    expand = expand,
    onExpandChange = onExpandChange,
    items = items,
    selectItem = selectItem,
    onSelectItemChange = onSelectItemChange
  )
}
