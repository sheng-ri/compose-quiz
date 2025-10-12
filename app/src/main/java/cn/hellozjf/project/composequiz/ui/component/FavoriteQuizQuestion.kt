package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import cn.hellozjf.project.composequiz.database.entity.Quiz

/**
 * 这是收藏的问答题目
 */
@Composable
fun FavoriteQuizQuestion(
  onExpandChange: (Boolean) -> Unit,
  expand: Boolean,
  quiz: Quiz,
  modifier: Modifier
) {
  Row(
    modifier = Modifier.clickable { onExpandChange(!expand) },
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = quiz.question,
      modifier = Modifier
        .weight(1f)
    )
    Icon(
      imageVector = Icons.Filled.ArrowDropDown,
      contentDescription = null,
      modifier.rotate(if (expand) 180f else 0f)
    )
  }
}