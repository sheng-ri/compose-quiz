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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.ui.component.QuizAnswerItem
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel

/**
 * 答案列表屏幕
 * TODO 这里需要优化一下，oldQuizList 改成 idList
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerScreen(
  title: String,
  oldQuizList: List<Quiz>,
  chapterViewModel: ChapterViewModel,
  chapterZhViewModel: ChapterZhViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  chapterQuizZhViewModel: ChapterQuizZhViewModel,
  chooseOptionMap: Map<Int, String>,
  quizOrderList: List<Int>,
  optionOrderList: List<List<Int>>,
  onNavigation: (NavKey) -> Unit,
  onClearBackStack: () -> Unit
) {

  val idList = remember(oldQuizList) {
    oldQuizList.map { it.id }
  }

  val quizList by chapterQuizViewModel.findByIdList(idList).collectAsState(listOf())

  // 这是所有的题目
  val quizDTOList = remember(quizList) {
    quizList.map {
      QuizDTO(
        id = it.id,
        chapterIndex = it.chapterIndex,
        question = it.question,
        correctOption = it.correctOption,
        wrongOption1 = it.wrongOption1,
        wrongOption2 = it.wrongOption2,
        wrongOption3 = it.wrongOption3,
        explanation = it.explanation
      )
    }
  }
  val listState = rememberLazyListState()
  var totalQuestionCount by remember { mutableStateOf(0) }
  var totalCorrectCount by remember { mutableStateOf(0) }

  var showMenu by remember { mutableStateOf(false) }
  var language by remember {mutableStateOf(LanguageConstant.EN)}

  // 下面这句话只会检查 quizDTOList 的内容，当内容不变时就不会重复执行
  LaunchedEffect(key1 = quizDTOList.hashCode()) {
    // 题目更新了，所以要计算一下正确和总的的题目数量
    totalQuestionCount = quizDTOList.size

    totalCorrectCount = 0
    for (quiz in quizDTOList) {
      if (quiz.correctOption == chooseOptionMap[quiz.id]) {
        // 这题答对了
        totalCorrectCount++
      } else {
        // 这题答错了，需要记录答错次数
        chapterQuizViewModel.incWrongAnswerCount(quiz.id)
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
              language = if (language == LanguageConstant.EN) {
                LanguageConstant.ZH
              } else {
                LanguageConstant.EN
              }
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.outline_language_24),
              contentDescription = "语言"
            )
            Text(
              text = language
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
            val quiz = quizList[order]
            val optionOrder = optionOrderList[order]
            item(key = quiz.id) {
              val selectOption = chooseOptionMap[quiz.id] ?: ""
              QuizAnswerItem(
                setFavorite = chapterQuizViewModel::setFavorite,
                index = index,
                quiz = quiz,
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