package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.hellozjf.project.composequiz.nav.ChapterQuizScreenKey
import cn.hellozjf.project.composequiz.nav.LaunchScreenKey
import cn.hellozjf.project.composequiz.nav.MainScreenKey
import cn.hellozjf.project.composequiz.nav.PunchScreenKey
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import cn.hellozjf.project.composequiz.viewmodel.PunchViewModel
import cn.hellozjf.project.composequiz.viewmodel.QuizViewModel

/**
 * 这个是最顶层的 Screen，它会根据 NavKey 显示具体的 Screen
 */
@Composable
fun NavDisplayScreen(
  chapterViewModel: ChapterViewModel,
  quizViewModel: QuizViewModel,
  configViewModel: ConfigViewModel,
  punchViewModel: PunchViewModel,
) {
  val backStack = rememberNavBackStack(LaunchScreenKey)
  val onNavigation: (NavKey) -> Unit = {
    backStack.add(it)
  }
  val onOpenMainScreen: () -> Unit = {
    // 清除栈中的启动页，添加首页
    backStack.clear()
    backStack.add(MainScreenKey)
  }
  val onClearBackStack: () -> Unit = {
    // 清除栈中除首页以外的页面
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
          quizViewModel = quizViewModel,
          configViewModel = configViewModel,
          punchViewModel = punchViewModel,
          onNavigation = onNavigation
        )
      }
      entry<QuizScreenKey> { key: QuizScreenKey ->
        // 根据传入的 quizKeyList，进行题目测验
        val titleEn = key.titleEn
        val titleZh = key.titleZh
        val quizKeyList = key.quizKeyList
        val onAnswerAllCorrect = key.onAnswerAllCorrect
        QuizScreen(
          titleEn = titleEn,
          titleZh = titleZh,
          quizKeyList = quizKeyList,
          chapterViewModel = chapterViewModel,
          quizViewModel = quizViewModel,
          configViewModel = configViewModel,
          onNavigation = onNavigation,
          onAnswerAllCorrect = onAnswerAllCorrect
        )
      }
      entry<QuizAnswerScreenKey> { key: QuizAnswerScreenKey ->
        val titleEn = key.titleEn
        val titleZh = key.titleZh
        val quizKeyList = key.quizKeyList
        val chooseOptionMap = key.chooseOptionMap
        val quizOrderList = key.quizOrderList
        val optionOrderList = key.optionOrderList
        val onAnswerAllCorrect = key.onAnswerAllCorrect
        AnswerScreen(
          titleEn = titleEn,
          titleZh = titleZh,
          quizKeyList = quizKeyList,
          chapterViewModel = chapterViewModel,
          quizViewModel = quizViewModel,
          configViewModel = configViewModel,
          chooseOptionMap = chooseOptionMap,
          quizOrderList = quizOrderList,
          optionOrderList = optionOrderList,
          onNavigation = onNavigation,
          onAnswerAllCorrect = onAnswerAllCorrect,
          onClearBackStack = onClearBackStack
        )
      }
      entry<ChapterQuizScreenKey> { key: ChapterQuizScreenKey ->
        // 展示某章所有的题目
        ChapterQuizScreen(
          chapterIndex = key.chapterIndex,
          configViewModel = configViewModel,
          chapterViewModel = chapterViewModel,
          quizViewModel = quizViewModel,
          onNavigation = onNavigation
        )
      }
      entry<LaunchScreenKey> { key: LaunchScreenKey ->
        // 展示启动页
        LaunchScreen(
          onOpenMainScreen = onOpenMainScreen
        )
      }
    }
  )
}