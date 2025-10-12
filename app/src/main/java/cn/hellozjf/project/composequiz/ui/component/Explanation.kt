package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * 这是问题答案的解释 组件
 */
@Composable
fun Explanation(explanation: String) {
  Text(
    text = "解释：$explanation"
  )
}