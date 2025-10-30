package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
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
import cn.hellozjf.project.composequiz.BuildConfig
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.dto.QuizDTO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionAndFavoriteRow(
  index: Int,
  quizDTO: QuizDTO,
  setFavorite: suspend (Int, Int, Boolean, Long) -> Unit
) {

  val coroutineScope = rememberCoroutineScope()
  val tooltipState = rememberTooltipState(isPersistent = false)

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .weight(1f)
        .clickable {
          if (BuildConfig.DEBUG) {
            coroutineScope.launch {
              tooltipState.show()
            }
          }
        }
    ) {
      TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
          // 这是气泡内显示的内容
          PlainTooltip {
            Text("${quizDTO.chapterIndex}-${quizDTO.quizIndex}")
          }
        },
        state = tooltipState,
      ) {
        Text(
          text = "${index + 1}. ${quizDTO.question}",
        )
      }
    }
    Image(
      painter = if (quizDTO.favorite) {
        painterResource(R.drawable.baseline_favorite_24)
      } else {
        painterResource(R.drawable.baseline_favorite_border_24)
      },
      contentDescription = if (quizDTO.favorite) "已收藏" else "未收藏", // 无障碍功能必需
      modifier = Modifier
        .size(32.dp)
        .clickable {
          coroutineScope.launch {
            setFavorite(
              quizDTO.chapterIndex,
              quizDTO.quizIndex,
              !quizDTO.favorite,
              System.currentTimeMillis()
            )
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
  var quizDTO by remember {
    mutableStateOf(
      QuizDTO(
        chapterIndex = 0,
        quizIndex = 1,
        question = "问题0",
        options = listOf(
          "正确答案",
          "错误答案1",
          "错误答案2",
          "错误答案3",
        ),
        correctOptionIndex = 0,
        explanation = "问题0解释",
        favorite = false,
        favoriteTime = 0L
      )
    )
  }
  QuestionAndFavoriteRow(
    index = 0,
    quizDTO = quizDTO,
    setFavorite = { chapterIndex, quizIndex, favorite, favoriteTime ->
      //if (index == 0) {
      quizDTO = quizDTO.copy(
        favorite = favorite,
        favoriteTime = favoriteTime
      )
      //}
    }
  )
}