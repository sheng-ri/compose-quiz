package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.ui.component.ChapterQuizColumn
import cn.hellozjf.project.composequiz.ui.component.MyTopAppBar
import cn.hellozjf.project.composequiz.util.ChapterUtils
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import cn.hellozjf.project.composequiz.viewmodel.QuizViewModel

/**
 * 章节题目 Screen
 */
@Composable
fun ChapterQuizScreen(
  chapterIndex: Int,
  configViewModel: ConfigViewModel,
  chapterViewModel: ChapterViewModel,
  quizViewModel: QuizViewModel,
  onNavigation: (NavKey) -> Unit,
) {

  val config by configViewModel.getConfigFlow().collectAsState(
    Config(language = LanguageConstant.EN)
  )

  val chapterDTO by chapterViewModel.findChapterDTOFlowByIndex(
    language = config.language,
    index = chapterIndex
  ).collectAsState(null)

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      MyTopAppBar(
        title = ChapterUtils.getChapterIndexStr(
          language = config.language,
          chapterIndex = chapterIndex
        ) + " " + (chapterDTO?.simpleTitle ?: ""),
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
      ChapterQuizColumn(
        language = config.language,
        chapterIndex = chapterIndex,
        findChapterDTOFlowByIndex = chapterViewModel::findChapterDTOFlowByIndex,
        findQuizDTOListFlowByChapterIndex = quizViewModel::findQuizDTOListFlowByChapterIndex,
        setLastTestChapterIndex = configViewModel::setLastTestChapterIndex,
        setFavorite = quizViewModel::setFavorite,
        onNavigation = onNavigation
      )
    }
  }
}