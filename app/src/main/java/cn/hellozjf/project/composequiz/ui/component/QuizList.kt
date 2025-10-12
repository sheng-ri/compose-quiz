package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.hellozjf.project.composequiz.database.entity.Quiz

/**
 * 问答列表
 */
@Composable
fun QuizList(
  quizList: List<Quiz>,
  quizOrder: List<Int>,
  quizSelectOptionMap: Map<Int, String>,
  onQuizSelectOptionChange: (Int, String) -> Unit,
  optionOrderList: List<List<Int>>,
  modifier: Modifier
) {
  // 滚动状态
  val listState = rememberLazyListState()
  LazyColumn(
    modifier = modifier,
    state = listState
  ) {
    if (quizList.isNotEmpty()) {
      quizOrder.forEachIndexed { index, order ->
        val quiz = quizList[order]
        item(key = quiz.id) {
          val selectOption = quizSelectOptionMap[quiz.id] ?: ""
          val onSelectOptionChange: (String) -> Unit = { newSelectOption ->
            onQuizSelectOptionChange(quiz.id, newSelectOption)
          }
          QuizListItem(
            index = index,
            quiz = quiz,
            selectOption = selectOption,
            onSelectOptionChange = onSelectOptionChange,
            optionOrder = optionOrderList[order]
          )
        }
      }
    }
  }
}