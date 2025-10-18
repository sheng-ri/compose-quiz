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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDropdown(
  expanded: Boolean,
  onExpandedChange: (Boolean) -> Unit,
  items: List<String>,
  selectedItem: String,
  onSelectedItemChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = onExpandedChange,
    modifier = modifier
  ) {
    TextField(
      value = selectedItem,
      onValueChange = {},
      modifier = Modifier
//        .fillMaxWidth()
        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
      readOnly = true,
      trailingIcon = {
        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
      }
    )

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { onExpandedChange(false) }
    ) {
      items.forEach { item ->
        DropdownMenuItem(
          text = { Text(text = item) },
          onClick = {
            onSelectedItemChange(item)
            onExpandedChange(false)
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
fun CommonDropdownPreview() {
  var expanded by remember { mutableStateOf(false) }
  val items: List<String> = listOf(
    "0", "1", "2", "3", "4"
  )
  var selectedItem by remember { mutableStateOf("0") }
  CommonDropdown(
    expanded = expanded,
    onExpandedChange = {
      expanded = it
    },
    items = items,
    selectedItem = selectedItem,
    onSelectedItemChange = {
      selectedItem = it
    }
  )
}