package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.nav.DestinationQuiz
import cn.hellozjf.project.composequiz.ui.component.ChapterList
import cn.hellozjf.project.composequiz.ui.component.DailyQuiz
import cn.hellozjf.project.composequiz.ui.component.FavoriteQuizPanel
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import kotlinx.coroutines.launch

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
  chapterZhViewModel: ChapterZhViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  configViewModel: ConfigViewModel,
  onNavigation: (NavKey) -> Unit
) {
  var destination by rememberSaveable { mutableStateOf(DestinationQuiz.DAILY_QUIZ) }
  val coroutineScope = rememberCoroutineScope()

  var showMenu by remember { mutableStateOf(false) }
  val configState = configViewModel.getConfigFlow().collectAsState(Config(
    language = LanguageConstant.EN
  ))

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Compose问答",
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = { /* 处理导航菜单点击 */ }) {
            Icon(
              imageVector = Icons.Default.Menu,
              contentDescription = "导航菜单"
            )
          }
        },
        actions = {
//          // 搜索按钮
//          IconButton(onClick = { /* 处理搜索 */ }) {
//            Icon(
//              imageVector = Icons.Default.Search,
//              contentDescription = "搜索"
//            )
//          }
          Row(
            modifier = Modifier.clickable {
              coroutineScope.launch {
                configViewModel.toggleLanguage()
              }
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.outline_language_24),
              contentDescription = "语言"
            )
            Text(
              text = configState.value?.language ?: LanguageConstant.EN
            )
          }

          // 更多选项按钮
          IconButton(onClick = { showMenu = !showMenu }) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "更多选项"
            )
          }

          // 下拉菜单
          DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
          ) {
            DropdownMenuItem(
              text = { Text("设置") },
              onClick = {
                showMenu = false
                // 处理设置点击
              },
              leadingIcon = {
                Icon(
                  Icons.Default.Favorite,
                  contentDescription = null
                )
              }
            )
            DropdownMenuItem(
              text = { Text("分享") },
              onClick = {
                showMenu = false
                // 处理分享点击
              },
              leadingIcon = {
                Icon(
                  Icons.Default.Share,
                  contentDescription = null
                )
              }
            )
          }
        },
//        colors = TopAppBarDefaults.topAppBarColors(
//          containerColor = MaterialTheme.colorScheme.primaryContainer,
//          titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//          actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//          navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//        )
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
            // TODO 这里需要根据当前的语言，选择具体的 viewModel
            val chapterList by chapterViewModel.findAllOrderByIndex().collectAsState(listOf())
            ChapterList(
              language = configState.value?.language ?: LanguageConstant.EN,
              chapterList = chapterList,
              findQuizDTOByChapterIndex = chapterQuizViewModel::findQuizDTOListByChapterIndex,
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
              findQuizEnByFavorite = chapterQuizViewModel::findQuizListByFavorite,
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
}