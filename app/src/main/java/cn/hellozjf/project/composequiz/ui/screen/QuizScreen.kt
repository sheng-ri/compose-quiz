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
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.ui.component.MyTopAppBar
import cn.hellozjf.project.composequiz.ui.component.QuizList
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.util.RandomConstant
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
    quizScreenViewModel.seed = RandomConstant.INVALID_SEED
    quizScreenViewModel.seed = seed
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
        quizOrder = quizScreenViewModel.quizOrder,
        quizSelectedOptionMap = quizScreenViewModel.quizSelectOption.toMap(),
        onQuizSelectedOptionChange = { mapKey, selectOption ->
          quizScreenViewModel.quizSelectOption[mapKey] = selectOption
        },
        optionOrderList = quizScreenViewModel.optionOrderList,
        modifier = Modifier.weight(1f)
      )

      Button(
        onClick = {
          onNavigation(
            QuizAnswerScreenKey(
              title = title,
              quizKeyList = quizKeyList,
              chooseOptionMap = quizScreenViewModel.quizSelectOption.toMap(),
              quizOrderList = quizScreenViewModel.quizOrder,
              optionOrderList = quizScreenViewModel.optionOrderList
            )
          )
        }
      ) {
        Text("提交")
      }
    }
  }
}

