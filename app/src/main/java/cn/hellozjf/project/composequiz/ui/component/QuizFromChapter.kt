package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cn.hellozjf.project.composequiz.database.entity.Chapter

/**
 * 问答来自哪一章
 */
@Composable
fun QuizFromChapter(
  chapter: Chapter
) {
  // val chapterList by chapterViewModel.findByIndex(chapterIndex).collectAsState(listOf())
  // if (chapterList.isNotEmpty()) {
  Text(
    text = "来自：第${chapter.index}章（${chapter.simpleTitle}）"
  )
  //  }
}