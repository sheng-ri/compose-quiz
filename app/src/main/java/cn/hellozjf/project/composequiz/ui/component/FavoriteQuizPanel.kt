package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.util.OrderMethodConstant

/**
 * 这是收藏的问答列表、排序方式、测试 组件
 */
@Composable
fun FavoriteQuizPanel(
  getChapterByIndex: suspend (Int) -> Chapter?,
  findByFavoriteOrderByChapterIndex: suspend () -> List<QuizEn>,
  findByFavoriteOrderByFavoriteTime: suspend () -> List<QuizEn>,
  findByFavoriteOrderByWrongAnswerCount: suspend () -> List<QuizEn>,
  findQuizEnByFavorite: suspend () -> List<QuizEn>,
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
    OrderMethodConstant.CHAPTER,
    OrderMethodConstant.FAVORITE_TIME,
    OrderMethodConstant.WRONG_ANSWER_COUNT
  )
  var orderMethodSelectText by remember { mutableStateOf(orderMethodItems[0]) }
  val onOrderMethodSelectTextChange: (String) -> Unit = {
    orderMethodSelectText = it
  }

  var quizEnList by remember { mutableStateOf(listOf<QuizEn>()) }

  LaunchedEffect(key1 = orderMethodSelectText) {
    val quizzes = when (orderMethodSelectText) {
      OrderMethodConstant.CHAPTER -> findByFavoriteOrderByChapterIndex()
      OrderMethodConstant.FAVORITE_TIME -> findByFavoriteOrderByFavoriteTime()
      OrderMethodConstant.WRONG_ANSWER_COUNT -> findByFavoriteOrderByWrongAnswerCount()
      else -> findByFavoriteOrderByChapterIndex()
    }
    quizEnList = quizzes
  }

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    Text(
      text = "每日测试",
      fontSize = 32.sp
    )
    OrderMethod(
      dropdownExpand = orderMethodExpand,
      onDropdownExpandChange = onOrderMethodExpandChange,
      orderMethods = orderMethodItems,
      selectedOrderMethod = orderMethodSelectText,
      onSelectedOrderMethodChange = onOrderMethodSelectTextChange
    )
    FavoriteQuizList(
      quizEnList = quizEnList,
      getChapterByIndex = getChapterByIndex,
      onNavigation = onNavigation,
      modifier = Modifier.weight(1f)
    )
    FavoriteTestRow(
      findQuizEnByFavorite = findQuizEnByFavorite,
      onNavigation = onNavigation,
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
fun FavoriteQuizPanelPreview() {
  val getChapterByIndex: suspend (Int) -> Chapter? = { chapterIndex ->
    Chapter(
      index = chapterIndex,
      fullTitle = "第${chapterIndex}章完整版标题",
      simpleTitle = "简化标题",
      simpleUrl = "http://xx.com/sim/${chapterIndex}",
      fullUrl = "http://xxx.com/full/${chapterIndex}"
    )
  }
  val quizEn00 = QuizEn(
    id = 0,
    chapterIndex = 0,
    quizIndex = 1,
    question = "第0章问题0",
    correctOption = "正确选项",
    wrongOption1 = "错误选项1",
    wrongOption2 = "错误选项2",
    wrongOption3 = "错误选项3",
    explanation = "第0章问题0解释",
    favorite = true,
    favoriteTime = 20L,
    wrongAnswerCount = 4
  )
  val quizEn01 = QuizEn(
    id = 1,
    chapterIndex = 0,
    quizIndex = 2,
    question = "第0章问题1",
    correctOption = "正确选项",
    wrongOption1 = "错误选项1",
    wrongOption2 = "错误选项2",
    wrongOption3 = "错误选项3",
    explanation = "第0章问题1解释",
    favorite = true,
    favoriteTime = 10L,
    wrongAnswerCount = 3
  )
  val quizEn10 = QuizEn(
    id = 10,
    chapterIndex = 1,
    quizIndex = 1,
    question = "第1章问题0",
    correctOption = "正确选项",
    wrongOption1 = "错误选项1",
    wrongOption2 = "错误选项2",
    wrongOption3 = "错误选项3",
    explanation = "第1章问题0解释",
    favorite = true,
    favoriteTime = 40L,
    wrongAnswerCount = 2
  )
  val quizEn11 = QuizEn(
    id = 11,
    chapterIndex = 1,
    quizIndex = 2,
    question = "第1章问题1",
    correctOption = "正确选项",
    wrongOption1 = "错误选项1",
    wrongOption2 = "错误选项2",
    wrongOption3 = "错误选项3",
    explanation = "第1章问题1解释",
    favorite = true,
    favoriteTime = 30L,
    wrongAnswerCount = 1
  )
  val rawList = listOf(quizEn00, quizEn01, quizEn10, quizEn11)
  val findByFavoriteOrderByChapterIndex: suspend () -> List<QuizEn> = {
    rawList.sortedBy { it.chapterIndex }
  }
  val findByFavoriteOrderByFavoriteTime: suspend () -> List<QuizEn> = {
    rawList.sortedBy { it.favoriteTime }
  }
  val findByFavoriteOrderByWrongAnswerCount: suspend () -> List<QuizEn> = {
    rawList.sortedBy { it.wrongAnswerCount }
  }
  val findQuizEnByFavorite: suspend () -> List<QuizEn> = {
    val list = mutableListOf<QuizEn>()
    for (i in 0 until 10) {
      val quizEn = QuizEn(
        id = i,
        chapterIndex = i,
        quizIndex = 1,
        question = "问题$i",
        correctOption = "正确答案",
        wrongOption1 = "错误答案1",
        wrongOption2 = "错误答案2",
        wrongOption3 = "错误答案3",
        explanation = "问题${i}解释",
        favorite = true,
        favoriteTime = System.currentTimeMillis()
      )
      list.add(quizEn)
    }
    list.toList()
  }
  FavoriteQuizPanel(
    getChapterByIndex = getChapterByIndex,
    findByFavoriteOrderByChapterIndex = findByFavoriteOrderByChapterIndex,
    findByFavoriteOrderByFavoriteTime = findByFavoriteOrderByFavoriteTime,
    findByFavoriteOrderByWrongAnswerCount = findByFavoriteOrderByWrongAnswerCount,
    findQuizEnByFavorite = findQuizEnByFavorite,
    onNavigation = {}
  )
}

@Preview(
  showBackground = true
)
@Composable
fun FavoriteQuizPanelTest() {
  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Box(
      modifier = Modifier.padding(innerPadding)
    ) {
      FavoriteQuizPanelPreview()
    }
  }
}