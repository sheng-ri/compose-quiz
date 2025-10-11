package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialyQuizScreen(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  contentDescription: String
) {

  var expand by remember { mutableStateOf(false) }
  val onExpandChange: (Boolean) -> Unit = {
    expand = it
  }
  // 默认先升序
  val items = listOf("章节", "收藏时间", "答错次数")
  var selectText by remember { mutableStateOf(items[0]) }
  val onSelectTextChange: (String) -> Unit = {
    selectText = it
  }

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    Text(
      text = "每日测试",
      fontSize = 32.sp
    )
    OrderMethodRow(
      expand = expand,
      onExpandChange = onExpandChange,
      items = items,
      selectText = selectText,
      onSelectTextChange = onSelectTextChange
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderMethodRow(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  items: List<String>,
  selectText: String,
  onSelectTextChange: (String) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically
  ) {
    Spacer(
      modifier = Modifier.size(16.dp)
    )
    Text("排序方式")
    Spacer(
      modifier = Modifier.size(16.dp)
    )
    ExposedDropdownMenuBox(
      expanded = expand,
      onExpandedChange = onExpandChange
    ) {
      TextField(
        value = selectText,
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
              onSelectTextChange(item)
              onExpandChange(false)
            }
          )
        }
      }
    }
  }
}