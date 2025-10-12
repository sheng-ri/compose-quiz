package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.util.TestCountConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 收藏列表的测试数量
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTestCount(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  items: List<String>,
  selectText: String,       // 这个是下拉框选中的文本
  onSelectTextChange: (String) -> Unit,
  onNavigation: (NavKey) -> Unit,
  chapterQuizViewModel: ChapterQuizViewModel,
) {

  // 这个是下拉框选中自定义时的搜索数量
  var customTestCount by remember { mutableStateOf(selectText) }
  val minValue = 0
  val maxValue = 999
  val coroutineScope = rememberCoroutineScope()

  Row(
    verticalAlignment = Alignment.CenterVertically
  ) {
    // 最左边是个下拉框
    Text("测试数量")
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
              if (item != TestCountConstant.CUSTOM) {
                customTestCount = item
              }
              onExpandChange(false)
            }
          )
        }
      }
    }
    // 当下拉框选择自定义时，需要显示自定义的数量
    if (selectText == TestCountConstant.CUSTOM) {
      OutlinedTextField(
        value = customTestCount,
        onValueChange = { newText ->
          val filtered = newText.filter { it.isDigit() }
          if (filtered.isNotEmpty()) {
            val num = filtered.toInt()
            if (num in minValue..maxValue) {
              customTestCount = num.toString()
            } else if (num < minValue) {
              customTestCount = minValue.toString()
            } else {
              customTestCount = maxValue.toString()
            }
          } else {
            customTestCount = minValue.toString()
          }
        },
        modifier = Modifier.width(64.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      )
    }
    // 定义空白占满剩余空间，以便把按钮定位到最右边
    Spacer(modifier = Modifier.weight(1f))
    // 最右边是个测试按钮
    Button(
      onClick = {
        coroutineScope.launch {
          val quizList = withContext(Dispatchers.IO) {
            val testCount = if (selectText == TestCountConstant.CUSTOM) {
              customTestCount
            } else {
              selectText
            }
            val favoriteQuizList = chapterQuizViewModel.findQuizByFavorite()
            val quizList = favoriteQuizList.shuffled().take(testCount.toInt())
            quizList
          }
          // 触发数据库查询
          onNavigation(
            QuizScreenKey(
              title = "收藏测试",
              quizList = quizList
            )
          )
        }
      }
    ) {
      Text("进行测试")
    }
  }
}