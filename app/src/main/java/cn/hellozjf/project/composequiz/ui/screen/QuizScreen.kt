package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.ui.component.QuizList
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import kotlinx.coroutines.launch

/**
 * 问答屏幕
 * TODO quizList 改成 idList
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  title: String,
  quizKeyList: List<QuizKey>,
  chapterViewModel: ChapterViewModel,
  chapterZhViewModel: ChapterZhViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  configViewModel: ConfigViewModel,
  onNavigation: (NavKey) -> Unit
) {

  // 这是问题列表，初始为空列表，当 LaunchedEffect 执行完毕之后，就能得到实际的问题列表了
  var quizDTOList by remember { mutableStateOf<List<QuizDTO>>(listOf()) }

  // 这是题目的顺序
  val quizOrder = remember(quizDTOList.size) {
    List(quizDTOList.size) { it }.shuffled()
  }
  // 这是各个题目选项的顺序
  val optionOrderList = remember(quizDTOList.size) {
    List(quizDTOList.size) {
      List(4) { it }.shuffled()
    }
  }
  // 问题ID选择的答案
  val quizSelectOption = remember { mutableStateMapOf<String, String>() }

  var showMenu by remember { mutableStateOf(false) }
  var configState = configViewModel.getConfigFlow().collectAsState(
    Config(
      language = LanguageConstant.EN
    )
  )

  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(key1 = Unit) {
    // 先把语言查出来
    val language = configViewModel.getConfig()?.language ?: LanguageConstant.EN
    // 根据语言选择对应的 quizViewModel
    val quizViewModel = chapterQuizViewModel
    // 根据 quizKeyList 查出 quizList
    quizKeyList.map {
      // TODO 不知道在干什么
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "章节测试",
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
    Column(
      modifier = Modifier.padding(innerPadding)
    ) {
//      Text(
//        text = title
//      )

      QuizList(
        quizDTOList = quizDTOList,
        quizOrder = quizOrder,
        quizSelectedOptionMap = quizSelectOption.toMap(),
        onQuizSelectedOptionChange = { id, selectOption ->
          quizSelectOption[id] = selectOption
        },
        optionOrderList = optionOrderList,
        modifier = Modifier.weight(1f)
      )

      Button(
        onClick = {
          onNavigation(
            QuizAnswerScreenKey(
              title = title,
              quizKeyList = quizKeyList,
              chooseOptionMap = quizSelectOption.toMap(),
              quizOrderList = quizOrder,
              optionOrderList = optionOrderList
            )
          )
        }
      ) {
        Text("提交")
      }
    }
  }
}

