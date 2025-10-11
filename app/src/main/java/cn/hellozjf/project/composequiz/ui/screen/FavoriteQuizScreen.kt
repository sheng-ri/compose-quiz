package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

@Composable
fun FavoriteQuizScreen(
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
) {

  var expand by remember { mutableStateOf(false) }
  val onExpandChange: (Boolean) -> Unit = {
    expand = it
  }
  // 默认先升序
  val items = listOf("章节", "收藏时间", "答错次数")
  var selectText by remember { mutableStateOf(items[0]) }
  val onSelectTextChange: (String) -> Unit = {
    selectText = it
  }
  // TODO collectAsState 不知道是不是要改成 collectAsStateWithLifecycle
  val quizList by when (selectText) {
    "章节" -> {
      chapterQuizViewModel.findByFavoriteOrderByChapterIndex().collectAsState(listOf())
    }

    "收藏时间" -> {
      chapterQuizViewModel.findByFavoriteOrderByFavoriteTime().collectAsState(listOf())
    }

    "答错次数" -> {
      chapterQuizViewModel.findByFavoriteOrderByWrongAnswerCount().collectAsState(listOf())
    }

    else -> {
      chapterQuizViewModel.findByFavoriteOrderByChapterIndex().collectAsState(listOf())
    }
  }
  val listState = rememberLazyListState()
  val questionExpandMap = remember { mutableStateMapOf<Int, Boolean>() }

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    Text(
      text = "每日测试",
      fontSize = 32.sp
    )
    OrderMethodRow(
      expand = expand,
      onExpandChange = onExpandChange,
      items = items,
      selectText = selectText,
      onSelectTextChange = onSelectTextChange
    )
    LazyColumn(
      modifier = Modifier.weight(1f),
      state = listState
    ) {
      if (quizList.isNotEmpty()) {
        quizList.forEachIndexed { index, quiz ->
          item(key = quiz.id) {
            val expand = questionExpandMap[quiz.id] ?: false
            val onExpandChange: (Boolean) -> Unit = {
              questionExpandMap[quiz.id] = it
            }
            Question(
              expand = expand,
              onExpandChange = onExpandChange,
              quiz = quiz
            )
          }
        }
      }
    }
  }
}

@Composable
fun Question(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  quiz: Quiz,
  modifier: Modifier = Modifier
) {
  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.onPrimary
    ),
    modifier = modifier
      .padding(3.dp)
      .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {

    Column {
      Row(
        modifier = Modifier.clickable { onExpandChange(!expand) },
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = quiz.question,
          modifier = Modifier
            .weight(1f)
        )
        Icon(
          imageVector = Icons.Filled.ArrowDropDown,
          contentDescription = null,
          modifier.rotate(if (expand) 180f else 0f)
        )
      }
      if (expand) {
        // 显示这题的所有选项，正确选项，解释，打错次数
        Column {
          QuizOption(
            option = quiz.correctOption,
            selectOption = quiz.correctOption,
            correctOption = quiz.correctOption
          )
          QuizOption(
            option = quiz.wrongOption1,
            selectOption = quiz.correctOption,
            correctOption = quiz.correctOption
          )
          QuizOption(
            option = quiz.wrongOption2,
            selectOption = quiz.correctOption,
            correctOption = quiz.correctOption
          )
          QuizOption(
            option = quiz.wrongOption3,
            selectOption = quiz.correctOption,
            correctOption = quiz.correctOption
          )
          Explanation(quiz.explanation)
          WrongAnswerCount(quiz.wrongAnswerCount)
        }
      }
    }
  }
}

@Composable
fun FromChapterIndex(chapterIndex: Int) {
  Text(
    text = "来自：第 $chapterIndex 章"
  )
}

@Composable
fun WrongAnswerCount(wrongAnswerCount: Int) {
  Text(
    text = "打错次数：$wrongAnswerCount"
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderMethodRow(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  items: List<String>,
  selectText: String,
  onSelectTextChange: (String) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically
  ) {
    Spacer(
      modifier = Modifier.size(16.dp)
    )
    Text("排序方式")
    Spacer(
      modifier = Modifier.size(16.dp)
    )
    ExposedDropdownMenuBox(
      expanded = expand,
      onExpandedChange = onExpandChange
    ) {
      TextField(
        value = selectText,
        onValueChange = {},
        modifier = Modifier
          .fillMaxWidth()
          .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
        readOnly = true,
        trailingIcon = {
          ExposedDropdownMenuDefaults.TrailingIcon(expanded = expand)
        }
      )

      ExposedDropdownMenu(
        expanded = expand,
        onDismissRequest = { onExpandChange(false) }
      ) {
        items.forEach { item ->
          DropdownMenuItem(
            text = { Text(text = item) },
            onClick = {
              onSelectTextChange(item)
              onExpandChange(false)
            }
          )
        }
      }
    }
  }
}