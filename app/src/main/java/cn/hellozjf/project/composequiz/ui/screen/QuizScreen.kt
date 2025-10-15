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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.R
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.ui.component.QuizList
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel

/**
 * 问答屏幕
 * TODO quizList 改成 idList
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  title: String,
  quizList: List<Quiz>,
  chapterViewModel: ChapterViewModel,
  chapterZhViewModel: ChapterZhViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  chapterQuizZhViewModel: ChapterQuizZhViewModel,
  onNavigation: (NavKey) -> Unit
) {

  // 这是题目的顺序
  val quizOrder = remember(quizList.size) {
    List(quizList.size) { it }.shuffled()
  }
  // 这是各个题目选项的顺序
  val optionOrderList = remember(quizList.size) {
    List(quizList.size) {
      List(4) { it }.shuffled()
    }
  }
  // 问题ID选择的答案
  val quizSelectOption = remember { mutableStateMapOf<Int, String>() }

  var showMenu by remember { mutableStateOf(false) }
  var language by remember {mutableStateOf(LanguageConstant.EN)}

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
//      Text(
//        text = title
//      )

      QuizList(
        quizList = quizList,
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
              quizList = quizList,
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

