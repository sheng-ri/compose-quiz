package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.util.OrderConstant
import cn.hellozjf.project.composequiz.util.TestCountConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * TODO
 * 在 Button 的 onClick 里调用 scope.launch 方法，来执行 viewModel 的 suspend 数据库查询方法，获取题目信息，然后 onNavigation 跳转是传递题目信息
 * 这个操作在 FavoriteQuizScreen 和 都要这样处理！！！！
 */
@Composable
fun FavoriteQuizScreen(
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
) {

  // 跟排序方式有关的状态
  var orderMethodExpand by remember { mutableStateOf(false) }
  val onOrderMethodExpandChange: (Boolean) -> Unit = {
    orderMethodExpand = it
  }
  // 默认先升序
  // TODO 后续增加一下升序降序？
  val orderMethodItems = listOf(
    OrderConstant.CHAPTER,
    OrderConstant.FAVORITE_TIME,
    OrderConstant.WRONG_ANSWER_COUNT
  )
  var orderMethodSelectText by remember { mutableStateOf(orderMethodItems[0]) }
  val onOrderMethodSelectTextChange: (String) -> Unit = {
    orderMethodSelectText = it
  }

  // 跟测试数量有关的状态
  var testCountExpand by remember { mutableStateOf(false) }
  val onTestCountExpandChange: (Boolean) -> Unit = {
    testCountExpand = it
  }
  val testCountItems = listOf(
    TestCountConstant.FIVE,
    TestCountConstant.TEN,
    TestCountConstant.TWENTY,
    TestCountConstant.CUSTOM,
  )
  var testCountSelectText by remember { mutableStateOf(testCountItems[0]) }
  val onTestCountSelectTextChange: (String) -> Unit = {
    testCountSelectText = it
  }

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    Text(
      text = "每日测试",
      fontSize = 32.sp
    )
    OrderMethodRow(
      expand = orderMethodExpand,
      onExpandChange = onOrderMethodExpandChange,
      items = orderMethodItems,
      selectText = orderMethodSelectText,
      onSelectTextChange = onOrderMethodSelectTextChange
    )
    QuestionList(
      selectText = orderMethodSelectText,
      chapterViewModel = chapterViewModel,
      chapterQuizViewModel = chapterQuizViewModel,
      onNavigation = onNavigation,
      modifier = Modifier.weight(1f)
    )
    TestCountRow(
      expand = testCountExpand,
      onExpandChange = onTestCountExpandChange,
      items = testCountItems,
      selectText = testCountSelectText,
      onSelectTextChange = onTestCountSelectTextChange,
      onNavigation = onNavigation,
      chapterQuizViewModel = chapterQuizViewModel
    )
  }
}

@Composable
fun QuestionList(
  selectText: String,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {
  val listState = rememberLazyListState()
  val questionExpandMap = remember { mutableStateMapOf<Int, Boolean>() }

  // TODO collectAsState 不知道是不是要改成 collectAsStateWithLifecycle
  val quizList by when (selectText) {
    OrderConstant.CHAPTER -> {
      chapterQuizViewModel.findByFavoriteOrderByChapterIndex().collectAsState(listOf())
    }

    OrderConstant.FAVORITE_TIME -> {
      chapterQuizViewModel.findByFavoriteOrderByFavoriteTime().collectAsState(listOf())
    }

    OrderConstant.WRONG_ANSWER_COUNT -> {
      chapterQuizViewModel.findByFavoriteOrderByWrongAnswerCount().collectAsState(listOf())
    }

    else -> {
      chapterQuizViewModel.findByFavoriteOrderByChapterIndex().collectAsState(listOf())
    }
  }

  LazyColumn(
    modifier = modifier,
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
            quiz = quiz,
            chapterViewModel = chapterViewModel,
            onNavigation = onNavigation
          )
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
  chapterViewModel: ChapterViewModel,
  onNavigation: (NavKey) -> Unit,
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
          FromChapter(
            chapterIndex = quiz.chapterIndex,
            chapterViewModel = chapterViewModel
          )
          WrongAnswerCount(quiz.wrongAnswerCount)
        }
      }
    }
  }
}

@Composable
fun FromChapter(
  chapterIndex: Int,
  chapterViewModel: ChapterViewModel
) {
  val chapterList by chapterViewModel.findByIndex(chapterIndex).collectAsState(listOf())
  if (chapterList.isNotEmpty()) {
    Text(
      text = "来自：第${chapterIndex}章（${chapterList[0].simpleTitle}）"
    )
  }
}

@Composable
fun WrongAnswerCount(wrongAnswerCount: Int) {
  Text(
    text = "答错次数：$wrongAnswerCount"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestCountRow(
  expand: Boolean,
  onExpandChange: (Boolean) -> Unit,
  items: List<String>,
  selectText: String,       // 这个是下拉框选中的文本
  onSelectTextChange: (String) -> Unit,
  onNavigation: (NavKey) -> Unit,
  chapterQuizViewModel: ChapterQuizViewModel,
) {

  // 这个是下拉框选中自定义时的搜索数量
  var customTestCount by remember { mutableStateOf(selectText) }
  val minValue = 0
  val maxValue = 999
  val coroutineScope = rememberCoroutineScope()

  Row(
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text("测试数量")
    ExposedDropdownMenuBox(
      expanded = expand,
      onExpandedChange = onExpandChange,
      modifier = Modifier.width(128.dp)
    ) {
      TextField(
        value = selectText,
        onValueChange = {},
        modifier = Modifier
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
              if (item != TestCountConstant.CUSTOM) {
                customTestCount = item
              }
              onExpandChange(false)
            }
          )
        }
      }
    }
    if (selectText == TestCountConstant.CUSTOM) {
      OutlinedTextField(
        value = customTestCount,
        onValueChange = { newText ->
          val filtered = newText.filter { it.isDigit() }
          if (filtered.isNotEmpty()) {
            val num = filtered.toInt()
            if (num in minValue..maxValue) {
              customTestCount = num.toString()
            } else if (num < minValue) {
              customTestCount = minValue.toString()
            } else {
              customTestCount = maxValue.toString()
            }
          } else {
            customTestCount = minValue.toString()
          }
        },
        modifier = Modifier.width(64.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      )
    }
    Spacer(modifier = Modifier.weight(1f))
    Button(
      onClick = {
        coroutineScope.launch {
          val quizList = withContext(Dispatchers.IO) {
            val testCount = if (selectText == TestCountConstant.CUSTOM) {
              customTestCount
            } else {
              selectText
            }
            val favoriteQuizList = chapterQuizViewModel.findQuizByFavorite()
            val quizList = favoriteQuizList.shuffled().take(testCount.toInt())
            quizList
          }
          // 触发数据库查询
          onNavigation(
            QuizScreenKey(
              title = "收藏测试",
              quizList = quizList
            )
          )
        }
      }
    ) {
      Text("进行测试")
    }
  }
}