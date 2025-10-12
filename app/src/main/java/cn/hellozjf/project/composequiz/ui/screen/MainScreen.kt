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
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.nav.DestinationQuiz
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.ui.component.ChapterList
import cn.hellozjf.project.composequiz.ui.component.DailyQuiz
import cn.hellozjf.project.composequiz.ui.component.FavoriteQuizPanel
import cn.hellozjf.project.composequiz.util.OrderConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * NavDisplayScreen 默认显示的 Screen
 * 它下面有 ChapterScreen、FavoriteQuizScreen、DailyQuizScreen 这三个 Screen
 * 通过 NavigationSuiteScaffold 最底部的 Icon 进行切换
 * TODO 找一下为什么 NavigationSuiteScaffold 无法通过滑动进行切换
 */
@Composable
fun MainScreen(
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
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
          val chapterList by chapterViewModel.findAllOrderByIndex().collectAsState(listOf())
          val onItemClick: (Chapter) -> Unit = { chapter ->
            coroutineScope.launch {
              // 在 IO 线程执行数据库查询
              val quizList = withContext(Dispatchers.IO) {
                chapterQuizViewModel.findQuizByChapterIndex(chapter.index)
              }
              onNavigation(
                QuizScreenKey(
                  title = chapter.simpleTitle,
                  quizList = quizList
                )
              )
            }
          }
          ChapterList(
            chapterList = chapterList,
            onItemClick = onItemClick
          )
        }

        DestinationQuiz.FAVORITE_QUIZ -> {
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