package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.hellozjf.project.composequiz.database.entity.Chapter

/**
 * 问答来自哪一章
 */
@Composable
fun QuizFromChapter(
  chapter: Chapter
) {
  Text(
    text = "来自：第${chapter.index}章（${chapter.simpleTitle}）"
  )
}

@Preview(
  showBackground = true
)
@Composable
fun QuizFromChapterPreview() {
  QuizFromChapter(
    chapter = Chapter(
      index = 0,
      simpleTitle = "标题0",
      fullTitle = "完全体标题0",
      simpleUrl = "http://xx.com/sim/0",
      fullUrl = "http://xx.com/full/0"
    )
  )
}