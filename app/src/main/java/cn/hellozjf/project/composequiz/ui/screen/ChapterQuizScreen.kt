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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
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
  val quizList by chapterQuizViewModel.searchResults.observeAsState(listOf())
  val listState = rememberLazyListState()

  LaunchedEffect(key1 = Unit) {
    // 搜索 chapterIndex 章节下面的题目
    chapterQuizViewModel.findQuizByChapter(chapterIndex)
  }

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
        quizList.forEachIndexed { index, quiz ->
          item {
            QuizListItem(
              id = quiz.id,
              index = index,
              question = quiz.question,
              correctOption = quiz.correctOption,
              wrongOption1 = quiz.wrongOption1,
              wrongOption2 = quiz.wrongOption2,
              wrongOption3 = quiz.wrongOption3
            )
          }
        }
      }

      Button(
        onClick = {}
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
  modifier: Modifier = Modifier
) {
  var selectedOption by remember { mutableStateOf("") }
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
      MyRadioButton(
        option = correctOption,
        selectedOption = selectedOption,
        onSelectedOptionChange = { newSelectOption ->
          selectedOption = newSelectOption
        }
      )
      MyRadioButton(
        option = wrongOption1,
        selectedOption = selectedOption,
        onSelectedOptionChange = { newSelectOption ->
          selectedOption = newSelectOption
        }
      )
      MyRadioButton(
        option = wrongOption2,
        selectedOption = selectedOption,
        onSelectedOptionChange = { newSelectOption ->
          selectedOption = newSelectOption
        }
      )
      MyRadioButton(
        option = wrongOption3,
        selectedOption = selectedOption,
        onSelectedOptionChange = { newSelectOption ->
          selectedOption = newSelectOption
        }
      )
    }
  }
}

@Composable
fun MyRadioButton(
  option: String,
  selectedOption: String,
  onSelectedOptionChange: (String) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelectedOptionChange(option) },
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = selectedOption == option,
      onClick = { onSelectedOptionChange(option) }
    )
    Text(
      text = option,
      modifier = Modifier.padding(start = 8.dp)
    )
  }
}