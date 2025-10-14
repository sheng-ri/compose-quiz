package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.ui.component.QuizAnswerItem
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel

/**
 * 答案列表屏幕
 * TODO 这里需要优化一下，oldQuizList 改成 idList
 */
@Composable
fun AnswerScreen(
  title: String,
  oldQuizList: List<Quiz>,
  chapterViewModel: ChapterViewModel,
  chapterZhViewModel: ChapterZhViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  chapterQuizZhViewModel: ChapterQuizZhViewModel,
  chooseOptionMap: Map<Int, String>,
  quizOrderList: List<Int>,
  optionOrderList: List<List<Int>>,
  onNavigation: (NavKey) -> Unit,
  onClearBackStack: () -> Unit
) {

  val idList = remember(oldQuizList) {
    oldQuizList.map { it.id }
  }

  val quizList by chapterQuizViewModel.findByIdList(idList).collectAsState(listOf())

  // 这是所有的题目
  val quizDTOList = remember(quizList) {
    quizList.map {
      QuizDTO(
        id = it.id,
        chapterIndex = it.chapterIndex,
        question = it.question,
        correctOption = it.correctOption,
        wrongOption1 = it.wrongOption1,
        wrongOption2 = it.wrongOption2,
        wrongOption3 = it.wrongOption3,
        explanation = it.explanation
      )
    }
  }
  val listState = rememberLazyListState()
  var totalQuestionCount by remember { mutableStateOf(0) }
  var totalCorrectCount by remember { mutableStateOf(0) }

  // 下面这句话只会检查 quizDTOList 的内容，当内容不变时就不会重复执行
  LaunchedEffect(key1 = quizDTOList.hashCode()) {
    // 题目更新了，所以要计算一下正确和总的的题目数量
    totalQuestionCount = quizDTOList.size

    totalCorrectCount = 0
    for (quiz in quizDTOList) {
      if (quiz.correctOption == chooseOptionMap[quiz.id]) {
        // 这题答对了
        totalCorrectCount++
      } else {
        // 这题答错了，需要记录答错次数
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
              QuizAnswerItem(
                setFavorite = chapterQuizViewModel::setFavorite,
                index = index,
                quiz = quiz,
                selectedOption = selectOption,
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