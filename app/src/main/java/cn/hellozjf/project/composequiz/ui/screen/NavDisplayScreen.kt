package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.hellozjf.project.composequiz.nav.MainScreenKey
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
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
      if (backStack.last() is QuizAnswerScreenKey) {
        // 从 QuizAnswerScreenKey 屏幕按回退按钮，返回主界面
        onClearBackStack()
      } else {
        // 其它屏幕按回退按钮，只返回上层
        backStack.removeLastOrNull()
      }
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
      entry<QuizAnswerScreenKey> { key: QuizAnswerScreenKey ->
        // val chapterIndex = key.chapterIndex
        val title = key.title
        val quizList = key.quizList
        val chooseOptionMap = key.chooseOptionMap
        val quizOrderList = key.quizOrderList
        val optionOrderList = key.optionOrderList
        QuizAnswerScreen(
          title = title,
          quizList = quizList,
          chapterViewModel = chapterViewModel,
          chapterQuizViewModel = chapterQuizViewModel,
          chooseOptionMap = chooseOptionMap,
          quizOrderList = quizOrderList,
          optionOrderList= optionOrderList,
          onNavigation = onNavigation,
          onClearBackStack = onClearBackStack
        )
      }
    }
  )
}