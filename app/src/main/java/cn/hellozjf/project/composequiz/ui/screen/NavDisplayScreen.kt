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
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel

/**
 * 这个是最顶层的 Screen，它会根据 NavKey 显示具体的 Screen
 */
@Composable
fun NavDisplayScreen(
  chapterViewModel: ChapterViewModel,
  chapterZhViewModel: ChapterZhViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  chapterQuizZhViewModel: ChapterQuizZhViewModel
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
          chapterZhViewModel = chapterZhViewModel,
          chapterQuizViewModel = chapterQuizViewModel,
          chapterQuizZhViewModel = chapterQuizZhViewModel,
          onNavigation = onNavigation
        )
      }
      entry<QuizScreenKey> { key: QuizScreenKey ->
        val title = key.title
        val quizList = key.quizList
        QuizScreen(
          title = title,
          quizList = quizList,
          chapterViewModel = chapterViewModel,
          chapterZhViewModel = chapterZhViewModel,
          chapterQuizViewModel = chapterQuizViewModel,
          chapterQuizZhViewModel = chapterQuizZhViewModel,
          onNavigation = onNavigation
        )
      }
      entry<QuizAnswerScreenKey> { key: QuizAnswerScreenKey ->
        val title = key.title
        val quizList = key.quizList
        val chooseOptionMap = key.chooseOptionMap
        val quizOrderList = key.quizOrderList
        val optionOrderList = key.optionOrderList
        AnswerScreen(
          title = title,
          oldQuizList = quizList,
          chapterViewModel = chapterViewModel,
          chapterZhViewModel = chapterZhViewModel,
          chapterQuizViewModel = chapterQuizViewModel,
          chapterQuizZhViewModel = chapterQuizZhViewModel,
          chooseOptionMap = chooseOptionMap,
          quizOrderList = quizOrderList,
          optionOrderList = optionOrderList,
          onNavigation = onNavigation,
          onClearBackStack = onClearBackStack
        )
      }
    }
  )
}