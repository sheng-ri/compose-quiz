package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel

@Composable
fun ChapterQuizScreen(
  chapterQuizViewModel: ChapterQuizViewModel,
  modifier: Modifier = Modifier
) {

  // TODO 这里要从 chapterQuizViewModel 获取所有的章节，显示在列表中
  // TODO 点击章节的时候，使用 chapterQuizViewModel 查询该章节下面所有的题目，进行问答测试

  Box(
    modifier = modifier.fillMaxSize()
  ) {
    Text(
      text = "章节测试",
      fontSize = 32.sp
    )
  }
}