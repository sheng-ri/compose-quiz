package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.nav.DestinationQuiz
import cn.hellozjf.project.composequiz.ui.component.ChapterList
import cn.hellozjf.project.composequiz.ui.component.DailyQuiz
import cn.hellozjf.project.composequiz.ui.component.FavoriteQuizPanel
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel

/**
 * NavDisplayScreen 默认显示的 Screen
 * 它下面有 ChapterScreen、FavoriteQuizScreen、DailyQuizScreen 这三个 Screen
 * 通过 NavigationSuiteScaffold 最底部的 Icon 进行切换
 * TODO 找一下为什么 NavigationSuiteScaffold 无法通过滑动进行切换
 */
@Composable
fun MainScreen(
  chapterViewModel: ChapterViewModel,
  chapterZhViewModel: ChapterZhViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  chapterQuizZhViewModel: ChapterQuizZhViewModel,
  onNavigation: (NavKey) -> Unit
) {
  var destination by rememberSaveable { mutableStateOf(DestinationQuiz.DAILY_QUIZ) }
  val coroutineScope = rememberCoroutineScope()

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
        // 或者使用 .safeDrawingPadding()，它包含了系统栏和刘海屏/打孔屏的安全区域
        .safeDrawingPadding()
        .fillMaxSize()
    ) {
      when (destination) {
        DestinationQuiz.CHAPTER_LIST -> {
          // 按章节号排序，查出所有的章节
          // TODO 这里需要根据当前的语言，选择具体的 viewModel
          val chapterList by chapterViewModel.findAllOrderByIndex().collectAsState(listOf())
          ChapterList(
            chapterList = chapterList,
            findQuizByChapterIndex = chapterQuizViewModel::findQuizByChapterIndex,
            onNavigation = onNavigation
          )
        }

        DestinationQuiz.FAVORITE_QUIZ -> {
          // TODO 这里需要根据当前的语言，选择具体的 viewModel
          FavoriteQuizPanel(
            getChapterByIndex = chapterViewModel::findByIndex,
            findByFavoriteOrderByChapterIndex = chapterQuizViewModel::findByFavoriteOrderByChapterIndex,
            findByFavoriteOrderByFavoriteTime = chapterQuizViewModel::findByFavoriteOrderByFavoriteTime,
            findByFavoriteOrderByWrongAnswerCount = chapterQuizViewModel::findByFavoriteOrderByWrongAnswerCount,
            findQuizByFavorite = chapterQuizViewModel::findQuizByFavorite,
            onNavigation = onNavigation
          )
        }

        DestinationQuiz.DAILY_QUIZ -> {
          DailyQuiz(
            icon = destination.icon,
            contentDescription = destination.contentDescription
          )
        }
      }
    }
  }
}