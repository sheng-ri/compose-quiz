package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

/**
 * 问答来自哪一章
 */
@Composable
fun QuizFromChapter(
  chapterIndex: Int,
  chapterViewModel: ChapterViewModel
) {
  val chapterList by chapterViewModel.findByIndex(chapterIndex).collectAsState(listOf())
  if (chapterList.isNotEmpty()) {
    Text(
      text = "来自：第${chapterIndex}章（${chapterList[0].simpleTitle}）"
    )
  }
}