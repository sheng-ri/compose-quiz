package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.ui.component.MyTopAppBar
import cn.hellozjf.project.composequiz.ui.component.QuizList
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import cn.hellozjf.project.composequiz.viewmodel.QuizScreenViewModel

/**
 * 问答屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  title: String,
  seed: Long,
  quizKeyList: List<QuizKey>,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  configViewModel: ConfigViewModel,
  quizScreenViewModel: QuizScreenViewModel,
  onNavigation: (NavKey) -> Unit
) {

  val config by configViewModel.getConfigFlow().collectAsState(
    Config(
      language = LanguageConstant.EN
    )
  )

  val language by remember {
    derivedStateOf {
      config.language
    }
  }

  // 这是题目的顺序
  val quizOrder by rememberSaveable(
    inputs = arrayOf(quizKeyList.joinToString(",")),
    stateSaver = Saver<List<Int>, String>(
      save = { intList -> intList.joinToString(",") },
      restore = { string -> string.split(",").map { s -> s.toInt() } }
    )
  ) {
    mutableStateOf(
      List(quizKeyList.size) {
        it
      }.shuffled()
    )
  }

  // 这是各个题目选项的顺序
  val optionOrderList by rememberSaveable(
    inputs = arrayOf(quizKeyList.joinToString(",")),
    stateSaver = Saver<List<List<Int>>, String>(
      save = { intListList ->
        intListList.joinToString("|") { intList ->
          intList.joinToString(",")
        }
      },
      restore = { string ->
        string.split("|").map { sList ->
          sList.split(",").map { s ->
            s.toInt()
          }
        }
      }
    )
  ) {
    mutableStateOf(
      List(quizKeyList.size) {
        List(4) { it }.shuffled()
      }
    )
  }

  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(key1 = language) {
    // 根据 quizKeyList 查出 quizList
    val dtoList = quizKeyList.mapNotNull {
      chapterQuizViewModel.findByKey(
        language = language,
        quizKey = it
      )
    }
    quizScreenViewModel.quizDTOList = dtoList
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      MyTopAppBar(
        title = "章节测试",
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
//      Text(
//        text = title
//      )

      QuizList(
        quizDTOList = quizScreenViewModel.quizDTOList,
        quizOrder = quizOrder,
        quizSelectedOptionMap = quizScreenViewModel.quizSelectOption.toMap(),
        onQuizSelectedOptionChange = { mapKey, selectOption ->
          quizScreenViewModel.quizSelectOption[mapKey] = selectOption
        },
        optionOrderList = optionOrderList,
        modifier = Modifier.weight(1f)
      )

      Button(
        onClick = {
          onNavigation(
            QuizAnswerScreenKey(
              title = title,
              quizKeyList = quizKeyList,
              chooseOptionMap = quizScreenViewModel.quizSelectOption.toMap(),
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

