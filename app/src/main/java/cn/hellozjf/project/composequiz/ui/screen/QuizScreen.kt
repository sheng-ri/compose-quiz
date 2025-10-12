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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.ui.component.QuizListItem
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

/**
 * 问答屏幕
 * TODO quizList 改成 idList
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
  // 问题ID选择的答案
  val quizSelectOption = remember { mutableStateMapOf<Int, String>() }

  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding)
    ) {
      Text(
        text = title
      )

      QuizList(
        quizList = quizList,
        quizOrder = quizOrder,
        quizSelectOptionMap = quizSelectOption.toMap(),
        onQuizSelectOptionChange = { id, selectOption ->
          quizSelectOption[id] = selectOption
        },
        optionOrderList = optionOrderList,
        modifier = Modifier.weight(1f)
      )

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

