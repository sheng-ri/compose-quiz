package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * 答错次数
 */
@Composable
fun WrongAnswerCount(wrongAnswerCount: Int) {
  Text(
    text = "答错次数：$wrongAnswerCount"
  )
}