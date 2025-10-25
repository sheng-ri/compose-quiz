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
import cn.hellozjf.project.composequiz.ui.component.ChapterQuizList
import cn.hellozjf.project.composequiz.ui.component.MyTopAppBar
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel

/**
 * 章节题目 Screen
 */
@Composable
fun ChapterQuizScreen(
  chapterIndex: Int,
  chapterSimpleTitle: String,
  configViewModel: ConfigViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit,
) {

  val config by configViewModel.getConfigFlow().collectAsState(
    Config(language = LanguageConstant.EN)
  )

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      MyTopAppBar(
        title = "章节题目预览",
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
      ChapterQuizList(
        language = config.language,
        chapterIndex = chapterIndex,
        chapterSimpleTitle = chapterSimpleTitle,
        findQuizDTOFlowByChapterIndex = chapterQuizViewModel::findQuizDTOFlowByChapterIndex,
        setLastTestChapterIndex = configViewModel::setLastTestChapterIndex,
        setFavorite = chapterQuizViewModel::setFavorite,
        onNavigation = onNavigation
      )
    }
  }
}