package cn.hellozjf.project.composequiz.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.ui.component.MyTopAppBar
import cn.hellozjf.project.composequiz.ui.component.QuizAnswerItem
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel

private val TAG = "AnswerScreen"

/**
 * 答案列表屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerScreen(
  title: String,
  // oldQuizDTOList: List<QuizDTO>,
  quizKeyList: List<QuizKey>,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  configViewModel: ConfigViewModel,
  chooseOptionMap: Map<String, String>,
  quizOrderList: List<Int>,
  optionOrderList: List<List<Int>>,
  onNavigation: (NavKey) -> Unit,
  onClearBackStack: () -> Unit
) {

  val config by configViewModel.getConfigFlow().collectAsState(
    Config(language = LanguageConstant.EN)
  )

  val quizList by chapterQuizViewModel.findFlowByKeyList(
    language = config.language,
    quizKeyList = quizKeyList
  ).collectAsState(listOf())
  // val quizList by chapterQuizViewModel.findByIdList(idList).collectAsState(listOf())

  val quizWithoutWrongAnswerCountList by remember {
    derivedStateOf {
      quizList.map {
        it.copy(wrongAnswerCount = 0)
      }
    }
  }

  // 这是所有的题目
  val listState = rememberLazyListState()
  var totalQuestionCount by remember { mutableStateOf(0) }
  var totalCorrectCount by remember { mutableStateOf(0) }

  var showMenu by remember { mutableStateOf(false) }

  val coroutineScope = rememberCoroutineScope()

  // 下面这句话只会检查 quizDTOList 的内容，当内容不变（除 wrongAnswerCount 以外）时就不会重复执行
  LaunchedEffect(key1 = quizWithoutWrongAnswerCountList.hashCode()) {
    // 题目更新了，所以要计算一下正确和总的的题目数量
    totalQuestionCount = quizList.size

    totalCorrectCount = 0
    for (quiz in quizList) {
      if (quiz.correctOption == chooseOptionMap[quiz.getMapKey()]) {
        // 这题答对了
        totalCorrectCount++
      } else {
        // 这题答错了，需要记录答错次数
        chapterQuizViewModel.incWrongAnswerCount(quiz.chapterIndex, quiz.quizIndex)
      }
    }
    Log.d(TAG, "totalCorrectCount = $totalCorrectCount")
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      MyTopAppBar(
        title = "答案",
        toggleLanguage = {
          configViewModel.toggleLanguage()
        },
        language = config.language
      )
    }
  ) { innerPadding ->
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
            val quizDTO = quizList[order]
            val optionOrder = optionOrderList[order]
            item(key = quizDTO.getMapKey()) {
              val selectOption = chooseOptionMap[quizDTO.getMapKey()] ?: ""
              QuizAnswerItem(
                setFavorite = chapterQuizViewModel::setFavorite,
                index = index,
                quizDTO = quizDTO,
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