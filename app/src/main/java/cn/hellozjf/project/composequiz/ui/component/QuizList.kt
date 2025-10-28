package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey

/**
 * 问答列表
 */
@Composable
fun QuizList(
  quizDTOList: List<QuizDTO>,     // 已经改变过问题顺序和选项顺序的 quizDTOList
  quizSelectedOptionMap: Map<QuizKey, OptionKey>,   // 问题已经选过的选项
  onQuizSelectedOptionChange: (QuizKey, OptionKey) -> Unit,   // 问题进行了选项选择
  modifier: Modifier = Modifier
) {
  // 滚动状态
  val listState = rememberLazyListState()
  LazyColumn(
    modifier = modifier.padding(4.dp),
    state = listState
  ) {
    quizDTOList.forEachIndexed { index, quizDTO ->
      val quizKey = quizDTO.getQuizKey()
      item(key = quizKey.toString()) {
        val selectOptionKey = quizSelectedOptionMap[quizKey]
        val onSelectOptionKeyChange: (OptionKey) -> Unit = { newSelectOption ->
          onQuizSelectedOptionChange(quizKey, newSelectOption)
        }
        QuizListItem(
          index = index,
          quizDTO = quizDTO,
          selectedOptionKey = selectOptionKey,
          onSelectedOptionKeyChange = onSelectOptionKeyChange
        )
      }
    }
  }
}

@Preview(
  showBackground = true
)
@Composable
fun QuizListPreview() {
  val quizDTOLists: List<QuizDTO> = listOf(
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 2,
      question = "问题2",
      options = listOf(
        "错误选项1",
        "错误选项2",
        "正确选项",
        "错误选项3",
      ),
      correctOptionIndex = 0,
      explanation = "问题2解释"
    ),
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 0,
      question = "问题0",
      options = listOf(
        "正确选项",
        "错误选项1",
        "错误选项2",
        "错误选项3",
      ),
      correctOptionIndex = 0,
      explanation = "问题0解释"
    ),
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 1,
      question = "问题1",
      options = listOf(
        "错误选项1",
        "正确选项",
        "错误选项2",
        "错误选项3",
      ),
      correctOptionIndex = 1,
      explanation = "问题1解释"
    ),
  )
  val quizSelectOptionMap = remember {
    mutableStateMapOf(
      QuizKey(0, 0) to OptionKey(0, 0, 0),
      QuizKey(0, 1) to OptionKey(0, 1, 1),
      QuizKey(0, 2) to OptionKey(0, 2, 2),
    )
  }
  val onQuizSelectOptionChange: (QuizKey, OptionKey) -> Unit = { key, selectOption ->
    quizSelectOptionMap[key] = selectOption
  }
  QuizList(
    quizDTOList = quizDTOLists,
    quizSelectedOptionMap = quizSelectOptionMap,
    onQuizSelectedOptionChange = onQuizSelectOptionChange,
  )
}