package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * 这是问题答案的解释 组件
 */
@Composable
fun Explanation(explanation: String) {
  Text(
    text = "解释：$explanation"
  )
}

@Preview(
  showBackground = true
)
@Composable
fun ExplanationPreview() {
  Explanation("这是一个解释组件")
}