package cn.hellozjf.project.composequiz.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.nav.DestinationQuiz
import cn.hellozjf.project.composequiz.ui.component.ChapterList
import cn.hellozjf.project.composequiz.ui.component.DailyQuiz
import cn.hellozjf.project.composequiz.ui.component.FavoriteQuizPanel
import cn.hellozjf.project.composequiz.ui.component.MyTopAppBar
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import cn.hellozjf.project.composequiz.viewmodel.PunchViewModel
import cn.hellozjf.project.composequiz.viewmodel.QuizViewModel

private val TAG = "MainScreen"

/**
 * NavDisplayScreen 默认显示的 Screen
 * 它下面有 ChapterScreen、FavoriteQuizScreen、DailyQuizScreen 这三个 Screen
 * 通过 NavigationSuiteScaffold 最底部的 Icon 进行切换
 * TODO 找一下为什么 NavigationSuiteScaffold 无法通过滑动进行切换
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  chapterViewModel: ChapterViewModel,
  quizViewModel: QuizViewModel,
  configViewModel: ConfigViewModel,
  punchViewModel: PunchViewModel,
  onNavigation: (NavKey) -> Unit
) {
  var destination by rememberSaveable { mutableStateOf(DestinationQuiz.DAILY_QUIZ) }
  val coroutineScope = rememberCoroutineScope()

  var showMenu by remember { mutableStateOf(false) }
  Log.d(TAG, "before configViewModel.getConfigFlow().collectAsState")
  val config by configViewModel.getConfigFlow().collectAsState(
    Config(
      language = LanguageConstant.EN
    )
  )
  Log.d(TAG, "after configViewModel.getConfigFlow().collectAsState")

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      MyTopAppBar(
        title = "Compose问答",
        toggleLanguage = {
          configViewModel.toggleLanguage()
        },
        language = config.language
      )
    }
  ) { innerPadding ->
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
//          // 或者使用 .safeDrawingPadding()，它包含了系统栏和刘海屏/打孔屏的安全区域
//          .safeDrawingPadding()
          .padding(innerPadding)
          .fillMaxSize()
      ) {
        when (destination) {
          DestinationQuiz.CHAPTER_LIST -> {
            // 按章节号排序，查出所有的章节
            val language = config.language
            val lastTestChapterIndex = config.lastTestChapterIndex
            val chapterList by chapterViewModel.findDTOFlowOrderByIndex(language)
              .collectAsState(listOf())
            ChapterList(
              language = language,
              lastTestChapterIndex = lastTestChapterIndex,
              chapterDTOList = chapterList,
              findQuizDTOByChapterIndex = quizViewModel::findQuizDTOListByChapterIndex,
              onNavigation = onNavigation
            )
          }

          DestinationQuiz.FAVORITE_QUIZ -> {
            FavoriteQuizPanel(
              language = config.language,
              getChapterDTOByIndex = chapterViewModel::findChapterDTOByIndex,
              findByFavoriteOrderByChapterIndex = quizViewModel::findDTOListByFavoriteOrderByChapterIndex,
              findByFavoriteOrderByFavoriteTime = quizViewModel::findDTOListByFavoriteOrderByFavoriteTime,
              findByFavoriteOrderByWrongAnswerCount = quizViewModel::findDTOListByFavoriteOrderByWrongAnswerCount,
              findQuizDTOByFavorite = quizViewModel::findQuizListByFavorite,
              onNavigation = onNavigation
            )
          }

          DestinationQuiz.DAILY_QUIZ -> {
            DailyQuiz(
              language = config.language,
              getByYearMonth = punchViewModel::getByYearMonth,
              exists = punchViewModel::exists,
              findAllQuizExt = quizViewModel::findAllQuizExt,
              onNavigation = onNavigation
            )
          }
        }
      }
    }
  }
}