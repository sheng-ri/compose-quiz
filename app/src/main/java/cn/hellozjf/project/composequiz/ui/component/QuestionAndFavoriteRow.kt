package cn.hellozjf.project.composequiz.ui.component

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipDefaults.rememberTooltipPositionProvider
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.hellozjf.project.composequiz.BuildConfig
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import kotlinx.coroutines.launch

private val TAG = "QuestionAndFavoriteRow"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionAndFavoriteRow(
  index: Int,
  quizDTO: QuizDTO,
  setFavorite: suspend (Int, Int, Boolean, Long) -> Unit
) {

  val coroutineScope = rememberCoroutineScope()
  val tooltipState = rememberTooltipState(isPersistent = false)
  val positionProvider = rememberTooltipPositionProvider(TooltipAnchorPosition.Above)

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
        positionProvider = positionProvider,
        tooltip = {
          // 这是气泡内显示的内容
          PlainTooltip {
            Text("${quizDTO.chapterIndex}-${quizDTO.quizIndex}")
          }
        },
        state = tooltipState,
      ) {
        val color = LocalContentColor.current
        Log.d(TAG, "color = $color")
        Text(
          text = "${index + 1}. ${quizDTO.question}",
        )
      }
    }
    FavoriteButton(
      isFavorite = quizDTO.favorite,
      onFavoriteChange = {
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

@Composable
fun FavoriteButton(
  isFavorite: Boolean,
  onFavoriteChange: (Boolean) -> Unit
) {
  IconButton(
    onClick = { onFavoriteChange(!isFavorite) }
  ) {
    Icon(
      imageVector = if (isFavorite) {
        Icons.Filled.Favorite
      } else {
        Icons.Filled.FavoriteBorder
      },
      contentDescription = if (isFavorite) "取消收藏" else "收藏",
      tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    )
  }
}

@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_NO,
  name = "浅色模式"
)
@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_YES,
  name = "深色模式"
)
@Composable
fun FavoriteButtonPreview() {
  var isFavorite by remember { mutableStateOf(false) }
  val onFavoriteChange: (Boolean) -> Unit = {
    isFavorite = it
  }
  ComposeQuizTheme {
    Surface {
      FavoriteButton(
        isFavorite = isFavorite,
        onFavoriteChange = onFavoriteChange
      )
    }
  }
}


@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_NO,
  name = "浅色模式"
)
@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_YES,
  name = "深色模式"
)
@Composable
fun QuestionAndFavoriteRowPreview() {
  var quizDTO by remember {
    mutableStateOf(
      QuizDTO(
        chapterIndex = 0,
        quizIndex = 1,
        question = "问题0",
        description = "",
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
  ComposeQuizTheme {
    Surface {
      QuestionAndFavoriteRow(
        index = 0,
        quizDTO = quizDTO,
        setFavorite = { chapterIndex, quizIndex, favorite, favoriteTime ->
          quizDTO = quizDTO.copy(
            favorite = favorite,
            favoriteTime = favoriteTime
          )
        }
      )
    }
  }
}