package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.ui.component.MyTopAppBar
import cn.hellozjf.project.composequiz.ui.component.QuizListColumn
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.QuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel

/**
 * 问答屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  title: String,
  quizKeyList: List<QuizKey>,
  chapterViewModel: ChapterViewModel,
  quizViewModel: QuizViewModel,
  configViewModel: ConfigViewModel,
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

  // 这是问题列表，初始为空列表，当 LaunchedEffect 执行完毕之后，就能得到实际的问题列表了
  var quizDTOList by remember {
    mutableStateOf<List<QuizDTO>>(listOf())
  }

  var quizSelectOption by rememberSaveable(
    inputs = arrayOf(quizKeyList.joinToString(",")),
    stateSaver = Saver<Map<QuizKey, OptionKey>, String>(
      save = { map ->
        map.map { "${it.key}:${it.value}" }.joinToString(",")
      },
      restore = { string ->
        val result: MutableMap<QuizKey, OptionKey> = mutableMapOf()
        string.split(",").map {
          val (key, value) = it.split(":")
          val (quizChapterIndex, quizQuizIndex) = key.split("_")
          val quizKey = QuizKey(
            chapterIndex = quizChapterIndex.toInt(),
            quizIndex = quizQuizIndex.toInt()
          )
          val (optionChapterIndex, optionQuizIndex, optionOptionIndex) = value.split("_")
          val optionKey = OptionKey(
            chapterIndex = optionChapterIndex.toInt(),
            quizIndex = optionQuizIndex.toInt(),
            optionIndex = optionOptionIndex.toInt(),
          )
          result.put(quizKey, optionKey)
        }
        result.toMap()
      }
    )
  ) {
    mutableStateOf<Map<QuizKey, OptionKey>>(mapOf())
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

  LaunchedEffect(key1 = language) {
    // 根据 quizKeyList 查出 quizList
    val quizDtoList = quizKeyList.mapNotNull {
      quizViewModel.findQuizDTOByKey(
        language = language,
        quizKey = it
      )
    }
    quizDTOList = quizDtoList
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      MyTopAppBar(
        // TODO 把标题改成具体的章节序号和名称
        title = "章节测试",
        toggleLanguage = {
          configViewModel.toggleLanguage()
        },
        language = config.language
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier.padding(innerPadding)
    ) {
      QuizListColumn(
        findQuizDTOByKey = quizViewModel::findQuizDTOByKey,
        quizOrder = quizOrder,
        quizSelectOption = quizSelectOption,
        onQuizSelectOptionChange = {
          quizSelectOption = it
        },
        optionOrderList = optionOrderList,
        title = title,
        quizKeyList = quizKeyList,
        onNavigation = onNavigation
      )
    }
  }
}

