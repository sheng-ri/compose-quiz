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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialyQuizScreen(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  contentDescription: String
) {

  var expanded by remember { mutableStateOf(false) }
  // 默认先升序
  val items = listOf("章节", "收藏时间", "答错次数")
  var selectedText by remember { mutableStateOf(items[0]) }

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    Text(
      text = "每日测试",
      fontSize = 32.sp
    )
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
        expanded = expanded,
        onExpandedChange = { expanded = it }
      ) {
        TextField(
          value = selectedText,
          onValueChange = {},
          modifier = Modifier
            .fillMaxWidth()
            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
          readOnly = true,
          trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
          }
        )

        ExposedDropdownMenu(
          expanded = expanded,
          onDismissRequest = { expanded = false }
        ) {
          items.forEach { item ->
            DropdownMenuItem(
              text = { Text(text = item) },
              onClick = {
                selectedText = item
                expanded = false
              }
            )
          }
        }
      }
    }
  }
}