package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.util.TestCountConstant

/**
 * TODO 两个下拉框合并成一个下拉框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTestDropdown(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  items: List<String>,
  selectText: String,       // 这个是下拉框选中的文本
  onSelectTextChange: (String) -> Unit,
) {
  ExposedDropdownMenuBox(
    expanded = expand,
    onExpandedChange = onExpandChange,
    modifier = Modifier.width(128.dp)
  ) {
    TextField(
      value = selectText,
      onValueChange = {},
      modifier = Modifier
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
            onSelectTextChange(item)
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
fun FavoriteTestDropdownPreview() {
  val items = listOf(
    TestCountConstant.FIVE,
    TestCountConstant.TEN,
    TestCountConstant.TWENTY,
    TestCountConstant.CUSTOM,
  )
  var expand by remember { mutableStateOf(false) }
  var selectText by remember { mutableStateOf(items[0]) }
  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    FavoriteTestDropdown(
      expand = expand,
      onExpandChange = {
        expand = it
      },
      items = items,
      selectText = selectText,
      onSelectTextChange = {
        selectText = it
      }
    )
  }
}