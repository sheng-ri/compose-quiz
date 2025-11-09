package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import cn.hellozjf.project.composequiz.dto.QuizDTO

/**
 * 收藏测试页面 问答的题目 组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteQuizQuestion(
  expanded: Boolean,
  onExpandedChange: (Boolean) -> Unit,
  quizDTO: QuizDTO,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = Modifier.clickable { onExpandedChange(!expanded) },
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = quizDTO.question
      )
    }
    Icon(
      imageVector = Icons.Filled.ArrowDropDown,
      contentDescription = null,
      modifier.rotate(if (expanded) 180f else 0f)
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
fun FavoriteQuizQuestionPreview() {
  var expand by remember { mutableStateOf(true) }
  FavoriteQuizQuestion(
    onExpandedChange = {
      expand = it
    },
    expanded = expand,
    quizDTO = QuizDTO(
      chapterIndex = 0,
      quizIndex = 1,
      question = "第0章题目的标题第0章题目的标题第0章题目的标题第0章题目的标题第0章题目的标题",
      description = "",
      options = listOf(
        "题目正确选项",
        "题目错误选项1",
        "题目错误选项2",
        "题目错误选项3",
      ),
      correctOptionIndex = 0,
      explanation = "题目的解释"
    )
  )
}