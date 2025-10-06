package cn.hellozjf.project.composequiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

@Composable
fun DialyQuizScreen(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  contentDescription: String
) {
  Box(
    modifier = modifier.fillMaxSize()
  ) {
    Text(
      text = "每日测试",
      fontSize = 32.sp
    )
  }
}