package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

/**
 * 题目
 */
@Composable
fun QuizScreen(
  title: String,
  quizList: List<Quiz>,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit
) {

  // 这是题目的顺序
  val quizOrder = remember(quizList.size) {
    List(quizList.size) { it }.shuffled()
  }
  // 这是各个题目选项的顺序
  val optionOrderList = remember(quizList.size) {
    List(quizList.size) {
      List(4) { it }.shuffled()
    }
  }
  val listState = rememberLazyListState()
  // 问题ID选择的答案
  val quizSelectOption = remember { mutableStateMapOf<Int, String>() }

  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding)
    ) {
      Text(
        text = title
      )

      LazyColumn(
        modifier = Modifier.weight(1f),
        state = listState
      ) {
        if (quizList.isNotEmpty()) {
          quizOrder.forEachIndexed { index, order ->
            val quiz = quizList[order]
            item(key = quiz.id) {
              val selectOption = quizSelectOption[quiz.id] ?: ""
              val onSelectOptionChange: (String) -> Unit = { newSelectOption ->
                quizSelectOption[quiz.id] = newSelectOption
              }
              QuizItem(
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

      Button(
        onClick = {
          onNavigation(
            QuizAnswerScreenKey(
              title = title,
              quizList = quizList,
              chooseOptionMap = quizSelectOption.toMap(),
              quizOrderList = quizOrder,
              optionOrderList = optionOrderList
            )
          )
        }
      ) {
        Text("提交")
      }
    }
  }
}

@Composable
fun QuizItem(
  index: Int,
  quiz: Quiz,
  selectOption: String,
  onSelectOptionChange: (String) -> Unit,
  optionOrder: List<Int>,
  modifier: Modifier = Modifier
) {
  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier = modifier
      .padding(3.dp)
      .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column {
      Text(
        text = "${index + 1}. ${quiz.question}"
      )
      val options = listOf(
        quiz.correctOption,
        quiz.wrongOption1,
        quiz.wrongOption2,
        quiz.wrongOption3
      )
      for (order in optionOrder) {
        MyRadioButton(
          option = options[order],
          selectOption = selectOption,
          onSelectOptionChange = onSelectOptionChange
        )
      }
    }
  }
}

@Composable
fun MyRadioButton(
  option: String,
  selectOption: String,
  onSelectOptionChange: (String) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelectOptionChange(option) },
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = selectOption == option,
      onClick = { onSelectOptionChange(option) }
    )
    Text(
      text = option,
      modifier = Modifier.padding(start = 8.dp)
    )
  }
}