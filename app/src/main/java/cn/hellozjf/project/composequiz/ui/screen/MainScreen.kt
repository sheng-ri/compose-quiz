package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.nav.DestinationQuiz
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

@Composable
fun MainScreen(
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit
) {
  var destination by rememberSaveable { mutableStateOf(DestinationQuiz.DAILY_QUIZ) }

  NavigationSuiteScaffold(
    navigationSuiteItems = {
      DestinationQuiz.entries.forEach {
        item(
          icon = {
            Icon(
              it.icon,
              contentDescription = it.contentDescription
            )
          },
          label = { Text(it.label) },
          selected = it == destination,
          onClick = { destination = it }
        )
      }
    }
  ) {
    Box(
      modifier = Modifier
        // 确保内容不会被系统UI（状态栏、系统导航栏）遮挡
        .windowInsetsPadding(WindowInsets.systemBars)
        // 或者使用 .safeDrawingPadding()，它包含了系统栏和刘海屏/打孔屏的安全区域
//         .safeDrawingPadding()
        .fillMaxSize()
    ) {
      when (destination) {
        DestinationQuiz.CHAPTER_QUIZ -> ChapterScreen(
          chapterViewModel = chapterViewModel,
          chapterQuizViewModel = chapterQuizViewModel,
          onNavigation = onNavigation,
          modifier = modifier
        )

        DestinationQuiz.FAVORITE_QUIZ -> FavoriteQuizScreen(
          modifier = modifier,
          icon = destination.icon,
          contentDescription = destination.contentDescription
        )

        DestinationQuiz.DAILY_QUIZ -> DialyQuizScreen(
          modifier = modifier,
          icon = destination.icon,
          contentDescription = destination.contentDescription
        )
      }
    }
  }
}