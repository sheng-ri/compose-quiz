package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.database.entity.Quiz
import kotlinx.coroutines.launch

@Composable
fun QuestionAndFavoriteRow(
  index: Int,
  quiz: Quiz,
  setFavorite: suspend (Int, Boolean, Long) -> Unit
) {

  val coroutineScope = rememberCoroutineScope()

  Row {
    Text(
      text = "${index + 1}. ${quiz.question}",
      modifier = Modifier.weight(1f)
    )
    Image(
      painter = if (quiz.favorite) {
        painterResource(R.drawable.baseline_favorite_24)
      } else {
        painterResource(R.drawable.baseline_favorite_border_24)
      },
      contentDescription = if (quiz.favorite) "已收藏" else "未收藏", // 无障碍功能必需
      modifier = Modifier
        .size(32.dp)
        .clickable {
          coroutineScope.launch {
            setFavorite(quiz.id, !quiz.favorite, System.currentTimeMillis())
          }
        }
    )
  }
}