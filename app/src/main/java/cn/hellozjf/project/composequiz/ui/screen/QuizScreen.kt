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
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import cn.hellozjf.project.composequiz.viewmodel.QuizViewModel

/**
 * 问答屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  titleEn: String,
  titleZh: String,
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

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      MyTopAppBar(
        title = if (language == LanguageConstant.ZH) {
          titleZh
        } else {
          titleEn
        },
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
        language = language,
        quizOrder = quizOrder,
        quizSelectOption = quizSelectOption,
        onQuizSelectOptionChange = {
          quizSelectOption = it
        },
        optionOrderList = optionOrderList,
        titleEn = titleEn,
        titleZh = titleZh,
        quizKeyList = quizKeyList,
        onNavigation = onNavigation
      )
    }
  }
}

