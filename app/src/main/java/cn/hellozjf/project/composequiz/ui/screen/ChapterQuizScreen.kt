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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

/**
 * 章节题目
 */
@Composable
fun ChapterQuizScreen(
  chapterIndex: Int,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit
) {

  // 这是所有的题目
  val quizList by chapterQuizViewModel.findQuizFlowByChapter(chapterIndex).collectAsState(listOf())
  // 这是题目的顺序
  val quizOrderList = remember(quizList.size) {
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
        text = "第 $chapterIndex 章"
      )

      LazyColumn(
        modifier = Modifier.weight(1f),
        state = listState
      ) {
        if (quizList.isNotEmpty()) {
          quizOrderList.forEachIndexed { index, order ->
            val quiz = quizList[order]
            item(key = quiz.id) {
              val selectOption = quizSelectOption[quiz.id] ?: ""
              val onSelectOptionChange: (String) -> Unit = { newSelectOption ->
                quizSelectOption[quiz.id] = newSelectOption
              }
              QuizListItem(
                id = quiz.id,
                index = index,
                question = quiz.question,
                correctOption = quiz.correctOption,
                wrongOption1 = quiz.wrongOption1,
                wrongOption2 = quiz.wrongOption2,
                wrongOption3 = quiz.wrongOption3,
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
              // chapterIndex = chapterIndex,
              title = "xxx",
              quizList = quizList,
              chooseOptionMap = quizSelectOption.toMap(),
              quizOrderList = quizOrderList,
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
fun QuizListItem(
  id: Int,
  index: Int,
  question: String,
  correctOption: String,
  wrongOption1: String,
  wrongOption2: String,
  wrongOption3: String,
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
        text = "${index + 1}. $question"
      )
      val options = listOf(correctOption, wrongOption1, wrongOption2, wrongOption3)
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