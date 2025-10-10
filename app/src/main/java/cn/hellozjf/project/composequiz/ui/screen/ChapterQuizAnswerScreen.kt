package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

/**
 * 章节题目答案
 */
@Composable
fun ChapterQuizAnswerScreen(
  chapterIndex: Int,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  chooseOptionMap: Map<Int, String>,
  onNavigation: (NavKey) -> Unit,
  onClearBackStack: () -> Unit
) {

  // 这是所有的题目
  val quizList by chapterQuizViewModel.searchResults.observeAsState(listOf())
  val listState = rememberLazyListState()
  var totalQuestionCount by remember { mutableStateOf(0) }
  var totalCorrectCount by remember { mutableStateOf(0) }

  LaunchedEffect(key1 = Unit) {
    // 搜索 chapterIndex 章节下面的题目
    chapterQuizViewModel.findQuizByChapter(chapterIndex)
  }

  LaunchedEffect(key1 = quizList) {
    // 题目更新了，所以要计算一下正确和总的的题目数量
    totalQuestionCount = quizList.size

    val map = mutableMapOf<Int, Quiz>()
    for (quiz in quizList) {
      map[quiz.id] = quiz
    }

    for ((key, value) in chooseOptionMap) {
      if (value == map[key]?.correctOption) {
        totalCorrectCount++
      }
    }
  }

  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding)
    ) {
      Text(
        text = "你的总分是：$totalCorrectCount / $totalQuestionCount"
      )

      LazyColumn(
        modifier = Modifier.weight(1f),
        state = listState
      ) {
        quizList.forEachIndexed { index, quiz ->
          item(key = quiz.id) {
            val selectOption = chooseOptionMap[quiz.id] ?: ""
            QuizAnswerListItem(
              id = quiz.id,
              index = index,
              question = quiz.question,
              correctOption = quiz.correctOption,
              wrongOption1 = quiz.wrongOption1,
              wrongOption2 = quiz.wrongOption2,
              wrongOption3 = quiz.wrongOption3,
              explanation = quiz.explanation,
              selectOption = selectOption
            )
          }
        }
      }

      Button(
        onClick = { onClearBackStack() }
      ) {
        Text("返回")
      }
    }
  }
}

@Composable
fun QuizAnswerListItem(
  id: Int,
  index: Int,
  question: String,
  correctOption: String,
  wrongOption1: String,
  wrongOption2: String,
  wrongOption3: String,
  explanation: String,
  selectOption: String,
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
      QuizOption(
        option = correctOption,
        selectOption = selectOption,
        correctOption = correctOption
      )
      QuizOption(
        option = wrongOption1,
        selectOption = selectOption,
        correctOption = correctOption
      )
      QuizOption(
        option = wrongOption2,
        selectOption = selectOption,
        correctOption = correctOption
      )
      QuizOption(
        option = wrongOption3,
        selectOption = selectOption,
        correctOption = correctOption
      )
      Text(
        text = "解释：$explanation"
      )
    }
  }
}

@Composable
fun QuizOption(
  option: String,
  selectOption: String,
  correctOption: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (option == selectOption) {
      Image(
        painter = painterResource(R.drawable.baseline_check_circle_24),
        contentDescription = "已选中", // 无障碍功能必需
        modifier = Modifier.size(32.dp)
      )
    } else {
      Image(
        painter = painterResource(R.drawable.baseline_circle_24),
        contentDescription = "未选中", // 无障碍功能必需
        modifier = Modifier.size(32.dp)
      )
    }
    Text(
      text = option,
      modifier = Modifier.padding(start = 8.dp),
      color = when (option) {
        correctOption -> {
          Color.Green
        }

        selectOption -> {
          Color.Red
        }

        else -> {
          Color.Black
        }
      }
    )
  }
}