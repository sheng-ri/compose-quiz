package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.hellozjf.project.composequiz.dto.QuizDTO

/**
 * 问答列表
 */
@Composable
fun QuizList(
  quizDTOList: List<QuizDTO>,
  quizOrder: List<Int>,
  quizSelectedOptionMap: Map<String, String>,
  onQuizSelectedOptionChange: (String, String) -> Unit,
  optionOrderList: List<List<Int>>,
  modifier: Modifier = Modifier
) {
  // 滚动状态
  val listState = rememberLazyListState()
  LazyColumn(
    modifier = modifier,
    state = listState
  ) {
    if (quizDTOList.isNotEmpty()) {
      quizOrder.forEachIndexed { index, order ->
        val quizDTO = quizDTOList[order]
        val key = quizDTO.getMapKey()
        item(key = key) {
          val selectOption = quizSelectedOptionMap[key] ?: ""
          val onSelectOptionChange: (String) -> Unit = { newSelectOption ->
            onQuizSelectedOptionChange(key, newSelectOption)
          }
          QuizListItem(
            index = index,
            quizDTO = quizDTO,
            selectedOption = selectOption,
            onSelectedOptionChange = onSelectOptionChange,
            optionOrder = optionOrderList[order]
          )
        }
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
      quizIndex = 1,
      question = "问题0",
      correctOption = "正确选项",
      wrongOption1 = "错误选项1",
      wrongOption2 = "错误选项2",
      wrongOption3 = "错误选项3",
      explanation = "问题0解释"
    ),
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 2,
      question = "问题1",
      correctOption = "正确选项",
      wrongOption1 = "错误选项1",
      wrongOption2 = "错误选项2",
      wrongOption3 = "错误选项3",
      explanation = "问题1解释"
    ),
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 3,
      question = "问题2",
      correctOption = "正确选项",
      wrongOption1 = "错误选项1",
      wrongOption2 = "错误选项2",
      wrongOption3 = "错误选项3",
      explanation = "问题2解释"
    ),
  )
  val quizOrder: List<Int> = listOf(2, 1, 0)
  val quizSelectOptionMap = remember {
    mutableStateMapOf(
      "0_0" to "正确答案"
    )
  }
  val onQuizSelectOptionChange: (String, String) -> Unit = { key, selectOption ->
    quizSelectOptionMap[key] = selectOption
  }
  val optionOrderList: List<List<Int>> = listOf(
    listOf(0, 3, 1, 2),
    listOf(3, 1, 2, 0),
    listOf(0, 1, 3, 2),
  )
  QuizList(
    quizDTOList = quizDTOLists,
    quizOrder = quizOrder,
    quizSelectedOptionMap = quizSelectOptionMap,
    onQuizSelectedOptionChange = onQuizSelectOptionChange,
    optionOrderList = optionOrderList
  )
}