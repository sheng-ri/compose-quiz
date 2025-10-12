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
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.R

@Composable
fun FavoriteQuizOption(
  option: String,
  selectOption: String,
  correctOption: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (option == selectOption) {
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
      color = when (option) {
        correctOption -> {
          Color.Green
        }

        selectOption -> {
          Color.Red
        }

        else -> {
          Color.Black
        }
      }
    )
  }
}