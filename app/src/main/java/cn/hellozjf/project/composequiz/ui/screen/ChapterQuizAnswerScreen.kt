package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
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
  quizOrderList: List<Int>,
  optionOrderList: List<List<Int>>,
  onNavigation: (NavKey) -> Unit,
  onClearBackStack: () -> Unit
) {

  // 这是所有的题目
  // TODO collectAsState 不知道是不是要改成 collectAsStateWithLifecycle
  val quizList by chapterQuizViewModel.findQuizByChapter(chapterIndex).collectAsState(listOf())
  val listState = rememberLazyListState()
  var totalQuestionCount by remember { mutableStateOf(0) }
  var totalCorrectCount by remember { mutableStateOf(0) }

  // TODO 我不知道这个是否一直会重复进入？？？
  LaunchedEffect(key1 = quizList) {
    // 题目更新了，所以要计算一下正确和总的的题目数量
    totalQuestionCount = quizList.size

    totalCorrectCount = 0
    for (quiz in quizList) {
      if (quiz.correctOption == chooseOptionMap[quiz.id]) {
        // 这题答对了
        totalCorrectCount++
      } else {
        // 这题答错了，需要记录答错次数
        // TODO 我在这里修改了 wrongAnswerCount，会导致 quizList 刷新，然后再次进入 LaunchedEffect，导致死循环
        chapterQuizViewModel.incWrongAnswerCount(quiz.id)
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
        if (quizList.isNotEmpty()) {
          quizOrderList.forEachIndexed { index, order ->
            val quiz = quizList[order]
            val optionOrder = optionOrderList[order]
            item(key = quiz.id) {
              val selectOption = chooseOptionMap[quiz.id] ?: ""
              QuizAnswerListItem(
                chapterQuizViewModel = chapterQuizViewModel,
                id = quiz.id,
                index = index,
                quiz = quiz,
                selectOption = selectOption,
                optionOrder = optionOrder
              )
            }
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
  chapterQuizViewModel: ChapterQuizViewModel,
  id: Int,
  index: Int,
  quiz: Quiz,
  selectOption: String,
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
      Row {
        Text(
          text = "${index + 1}. ${quiz.question}",
          modifier = Modifier.weight(1f)
        )
        Image(
          painter = if (quiz.favorite) {
            painterResource(R.drawable.baseline_favorite_24)
          } else {
            painterResource(R.drawable.baseline_favorite_border_24)
          },
          contentDescription = if (quiz.favorite) "已收藏" else "未收藏", // 无障碍功能必需
          modifier = Modifier
            .size(32.dp)
            .clickable {
              chapterQuizViewModel.setFavorite(id, !quiz.favorite, System.currentTimeMillis())
            }
        )
      }
      val optionList = listOf(quiz.correctOption, quiz.wrongOption1, quiz.wrongOption2, quiz.wrongOption3)
      for (order in optionOrder) {
        QuizOption(
          option = optionList[order],
          selectOption = selectOption,
          correctOption = quiz.correctOption
        )
      }
      Text(
        text = "解释：${quiz.explanation}"
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