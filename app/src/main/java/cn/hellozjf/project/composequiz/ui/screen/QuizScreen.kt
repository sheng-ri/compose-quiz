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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.ui.component.MyTopAppBar
import cn.hellozjf.project.composequiz.ui.component.QuizList
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel

/**
 * 问答屏幕
 * TODO quizList 改成 idList
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  title: String,
  quizKeyList: List<QuizKey>,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  configViewModel: ConfigViewModel,
  onNavigation: (NavKey) -> Unit
) {

  // 这是问题列表，初始为空列表，当 LaunchedEffect 执行完毕之后，就能得到实际的问题列表了
  var quizDTOList by remember { mutableStateOf<List<QuizDTO>>(listOf()) }

  // 这是题目的顺序
  val quizOrder = remember(quizDTOList.size) {
    List(quizDTOList.size) { it }.shuffled()
  }
  // 这是各个题目选项的顺序
  val optionOrderList = remember(quizDTOList.size) {
    List(quizDTOList.size) {
      List(4) { it }.shuffled()
    }
  }
  // 问题ID选择的答案
  val quizSelectOption = remember { mutableStateMapOf<String, String>() }

  var showMenu by remember { mutableStateOf(false) }
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

  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(key1 = language) {
    // 根据 quizKeyList 查出 quizList
    val dtoList = quizKeyList.mapNotNull {
      chapterQuizViewModel.findByKey(
        language = language,
        quizKey = it
      )
    }
    quizDTOList = dtoList
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
        quizDTOList = quizDTOList,
        quizOrder = quizOrder,
        quizSelectedOptionMap = quizSelectOption.toMap(),
        onQuizSelectedOptionChange = { id, selectOption ->
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
              quizKeyList = quizKeyList,
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

