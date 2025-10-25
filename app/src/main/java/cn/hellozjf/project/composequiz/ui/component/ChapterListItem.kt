package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.nav.ChapterQuizScreenKey
import cn.hellozjf.project.composequiz.util.LanguageConstant

/**
 * 这是 章节测试 页面的章节列表项目 组件
 */
@Composable
fun ChapterListItem(
  language: String,
  lastTestChapterIndex: Int?,
  chapterDTO: ChapterDTO,
  findQuizDTOByChapterIndex: suspend (String, Int) -> List<QuizDTO>,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {

  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier = modifier
      .padding(3.dp)
      .fillMaxWidth()
      .clickable {
        onNavigation(
          ChapterQuizScreenKey(
            chapterIndex = chapterDTO.index,
            chapterSimpleTitle = chapterDTO.simpleTitle
          )
        )
      },
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (language == LanguageConstant.ZH) {
          "第 ${chapterDTO.index} 章"
        } else {
          "Ch. ${chapterDTO.index}"
        },
        modifier = Modifier.width(75.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = chapterDTO.simpleTitle,
        color = if (lastTestChapterIndex == chapterDTO.index) {
          MaterialTheme.colorScheme.primary
        } else {
          Color.Unspecified
        },
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(8.dp)
      )
      Spacer(modifier = Modifier.weight(1f))
//      if (lastTestChapterIndex == chapterDTO.index) {
//        Text(
//          text = "上次测试",
//          modifier = Modifier.padding(8.dp)
//        )
//      }
    }
  }
}

@Preview
@Composable
fun ChapterListItemPreview() {
  val chapterDTO = ChapterDTO(
    index = 0,
    simpleTitle = "简单标题0",
    fullTitle = "完全标题0",
    simpleUrl = "http://xxx.com/sim/0",
    fullUrl = "http://xxx.com/full/0"
  )
  val findQuizDTOByChapterIndex: suspend (String, Int) -> List<QuizDTO> =
    { language, chapterIndex ->
      val result = mutableListOf<QuizDTO>()
      for (i in 0 until 10) {
        val quizDTO = QuizDTO(
          chapterIndex = chapterIndex,
          quizIndex = i + 1,
          question = "章节${chapterIndex}问题${i}",
          correctOption = "问题${i}正确选项",
          wrongOption1 = "问题${i}错误选项1",
          wrongOption2 = "问题${i}错误选项2",
          wrongOption3 = "问题${i}错误选项3",
          explanation = "章节${chapterIndex}问题${i}解释"
        )
        result.add(quizDTO)
      }
      result.toList()
    }
  ChapterListItem(
    language = LanguageConstant.ZH,
    lastTestChapterIndex = 0,
    chapterDTO = chapterDTO,
    findQuizDTOByChapterIndex = findQuizDTOByChapterIndex,
    onNavigation = {}
  )
}