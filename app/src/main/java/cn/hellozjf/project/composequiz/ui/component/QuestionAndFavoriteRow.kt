package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import kotlinx.coroutines.launch

@Composable
fun QuestionAndFavoriteRow(
  index: Int,
  quizEn: QuizEn,
  setFavorite: suspend (Int, Boolean, Long) -> Unit
) {

  val coroutineScope = rememberCoroutineScope()

  Row(
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "${index + 1}. ${quizEn.question}",
      modifier = Modifier.weight(1f)
    )
    Image(
      painter = if (quizEn.favorite) {
        painterResource(R.drawable.baseline_favorite_24)
      } else {
        painterResource(R.drawable.baseline_favorite_border_24)
      },
      contentDescription = if (quizEn.favorite) "已收藏" else "未收藏", // 无障碍功能必需
      modifier = Modifier
        .size(32.dp)
        .clickable {
          coroutineScope.launch {
            setFavorite(quizEn.id, !quizEn.favorite, System.currentTimeMillis())
          }
        }
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
fun QuestionAndFavoriteRowPreview() {
  var quizEn by remember {
    mutableStateOf(
      QuizEn(
        id = 0,
        chapterIndex = 0,
        quizIndex = 1,
        question = "问题0",
        correctOption = "正确答案",
        wrongOption1 = "错误答案1",
        wrongOption2 = "错误答案2",
        wrongOption3 = "错误答案3",
        explanation = "问题0解释",
        favorite = false,
        favoriteTime = 0L
      )
    )
  }
  QuestionAndFavoriteRow(
    index = 0,
    quizEn = quizEn,
    setFavorite = { index, favorite, favoriteTime ->
      if (index == 0) {
        quizEn = quizEn.copy(
          favorite = favorite,
          favoriteTime = favoriteTime
        )
      }
    }
  )
}