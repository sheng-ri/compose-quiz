package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import cn.hellozjf.project.composequiz.ui.component.QuizAnswerItem
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import kotlinx.coroutines.launch

/**
 * 答案列表屏幕
 * TODO 这里需要优化一下，oldQuizDTOList 改成 keyList
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerScreen(
  title: String,
  // oldQuizDTOList: List<QuizDTO>,
  quizKeyList: List<QuizKey>,
  chapterViewModel: ChapterViewModel,
  chapterZhViewModel: ChapterZhViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  configViewModel: ConfigViewModel,
  chooseOptionMap: Map<String, String>,
  quizOrderList: List<Int>,
  optionOrderList: List<List<Int>>,
  onNavigation: (NavKey) -> Unit,
  onClearBackStack: () -> Unit
) {

  val config by configViewModel.getConfigFlow().collectAsState(
    Config(language = LanguageConstant.EN)
  )

  val quizList by chapterQuizViewModel.findFlowByKeyList(
    language = config?.language ?: LanguageConstant.EN,
    quizKeyList = quizKeyList
  ).collectAsState(listOf())
  // val quizList by chapterQuizViewModel.findByIdList(idList).collectAsState(listOf())

  // 这是所有的题目
  val quizDTOList = quizList
  val listState = rememberLazyListState()
  var totalQuestionCount by remember { mutableStateOf(0) }
  var totalCorrectCount by remember { mutableStateOf(0) }

  var showMenu by remember { mutableStateOf(false) }
  var configState = configViewModel.getConfigFlow().collectAsState(
    Config(
      language = LanguageConstant.EN
    )
  )

  val coroutineScope = rememberCoroutineScope()

  // 下面这句话只会检查 quizDTOList 的内容，当内容不变时就不会重复执行
  LaunchedEffect(key1 = quizDTOList.hashCode()) {
    // 题目更新了，所以要计算一下正确和总的的题目数量
    totalQuestionCount = quizDTOList.size

    totalCorrectCount = 0
    for (quiz in quizDTOList) {
      if (quiz.correctOption == chooseOptionMap[quiz.getMapKey()]) {
        // 这题答对了
        totalCorrectCount++
      } else {
        // 这题答错了，需要记录答错次数
        chapterQuizViewModel.incWrongAnswerCount(quiz.chapterIndex, quiz.quizIndex)
      }
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "答案",
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
    Column(
      modifier = Modifier.padding(innerPadding)
    ) {
      Text(
        text = "你的总分是：$totalCorrectCount / $totalQuestionCount"
      )

      LazyColumn(
        modifier = Modifier.weight(1f),
        state = listState
      ) {
        if (quizList.isNotEmpty()) {
          quizOrderList.forEachIndexed { index, order ->
            val quizDTO = quizList[order]
            val optionOrder = optionOrderList[order]
            item(key = quizDTO.getMapKey()) {
              val selectOption = chooseOptionMap[quizDTO.getMapKey()] ?: ""
              QuizAnswerItem(
                setFavorite = chapterQuizViewModel::setFavorite,
                index = index,
                quizDTO = quizDTO,
                selectedOption = selectOption,
                optionOrder = optionOrder
              )
            }
          }
        }
      }

      Button(
        onClick = { onClearBackStack() }
      ) {
        Text("返回")
      }
    }
  }
}