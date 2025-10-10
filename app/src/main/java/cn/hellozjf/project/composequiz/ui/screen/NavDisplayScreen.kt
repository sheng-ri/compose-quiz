package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.hellozjf.project.composequiz.nav.MainScreenKey
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

@Composable
fun NavDisplayScreen(
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel
) {
  val backStack = rememberNavBackStack(MainScreenKey)
  val onNavigation: (NavKey) -> Unit = {
    backStack.add(it)
  }
  val onClearBackStack: () -> Unit = {
    while (backStack.size > 1) {
      backStack.removeLastOrNull()
    }
  }
  NavDisplay(
    backStack = backStack,
    onBack = {
      backStack.removeLastOrNull()
    },
    entryProvider = entryProvider {
      entry<MainScreenKey> {
        MainScreen(
          chapterViewModel = chapterViewModel,
          chapterQuizViewModel = chapterQuizViewModel,
          onNavigation = onNavigation
        )
      }
      entry<QuizScreenKey> { key: QuizScreenKey ->
        val chapterIndex = key.chapterIndex
        ChapterQuizScreen(
          chapterIndex = chapterIndex,
          chapterViewModel = chapterViewModel,
          chapterQuizViewModel = chapterQuizViewModel,
          onNavigation = onNavigation
        )
      }
    }
  )
}