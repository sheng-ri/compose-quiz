package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * 答错次数
 */
@Composable
fun WrongAnswerCount(wrongAnswerCount: Int) {
  Text(
    text = "答错次数：$wrongAnswerCount"
  )
}

@Preview(
  showBackground = true
)
@Composable
fun WrongAnswerCountPreview() {
  WrongAnswerCount(10)
}