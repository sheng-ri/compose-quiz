package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.hellozjf.project.composequiz.BuildConfig
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.QuizDTO
import kotlinx.coroutines.launch

/**
 * 问答来自哪一章
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizFromChapter(
  chapterDTO: ChapterDTO,
  quizDTO: QuizDTO
) {

  val coroutineScope = rememberCoroutineScope()

  // isPersistent=true 允许多次点击显示/隐藏
  val tooltipState = rememberTooltipState(isPersistent = false)
  TooltipBox(
    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
    tooltip = {
      if (BuildConfig.DEBUG) {
        // 这是气泡内显示的内容
        PlainTooltip {
          Text("${quizDTO.chapterIndex}-${quizDTO.quizIndex}")
        }
      }
    },
    state = tooltipState,
  ) {
    Text(
      text = "来自：第${chapterDTO.index}章（${chapterDTO.simpleTitle}）",
      modifier = Modifier
        .clickable {
          if (BuildConfig.DEBUG) {
            coroutineScope.launch {
              tooltipState.show()
            }
          }
        }
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
fun QuizFromChapterPreview() {
  QuizFromChapter(
    chapterDTO = ChapterDTO(
      index = 0,
      simpleTitle = "标题0",
      fullTitle = "完全体标题0",
      simpleUrl = "http://xx.com/sim/0",
      fullUrl = "http://xx.com/full/0"
    ),
    quizDTO = QuizDTO(
      chapterIndex = 0,
      quizIndex = 0,
      question = "哈哈哈",
      options = listOf(),
      correctOptionIndex = 0,
      explanation = "",
    )
  )
}