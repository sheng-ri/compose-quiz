package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.dto.OptionKey

/**
 * 收藏测试页面 题目的选项 组件
 */
@Composable
fun FavoriteQuizOption(
  option: String,
  optionKey: OptionKey,
  selectedOptionKey: OptionKey?,
  correctOptionKey: OptionKey
) {
  Row(
    modifier = Modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (optionKey == selectedOptionKey) {
      Image(
        painter = painterResource(R.drawable.baseline_check_circle_24),
        contentDescription = "已选中", // 无障碍功能必需
        modifier = Modifier.size(32.dp)
      )
    } else {
      Image(
        painter = painterResource(R.drawable.baseline_circle_24),
        contentDescription = "未选中", // 无障碍功能必需
        modifier = Modifier.size(32.dp)
      )
    }
    Text(
      text = option,
      modifier = Modifier.padding(start = 8.dp),
      color = when (optionKey) {
        correctOptionKey -> {
          Color.Green
        }

        selectedOptionKey -> {
          Color.Red
        }

        else -> {
          Color.Black
        }
      }
    )
  }
}

@Preview(
  name = "正确的选项",
  showBackground = true
)
@Composable
fun FavoriteQuizOptionPreview() {
  FavoriteQuizOption(
    option = "选项1",
    optionKey = OptionKey(1, 1, 1),
    selectedOptionKey = OptionKey(1, 1, 1),
    correctOptionKey = OptionKey(1, 1, 1),
  )
}

@Preview(
  name = "错误的选项",
  showBackground = true
)
@Composable
fun FavoriteQuizOptionPreview3() {
  FavoriteQuizOption(
    option = "选项1",
    optionKey = OptionKey(1, 1, 1),
    selectedOptionKey = null,
    correctOptionKey = OptionKey(1, 1, 2)
  )
}