package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.util.OrderConstant
import cn.hellozjf.project.composequiz.util.TestCountConstant

/**
 * 这是收藏的问答列表、排序方式、测试 组件
 */
@Composable
fun FavoriteQuizPanel(
  getChapterByIndex: suspend (Int) -> Chapter?,
  findByFavoriteOrderByChapterIndex: suspend () -> List<Quiz>,
  findByFavoriteOrderByFavoriteTime: suspend () -> List<Quiz>,
  findByFavoriteOrderByWrongAnswerCount: suspend () -> List<Quiz>,
  findQuizByFavorite: suspend () -> List<Quiz>,
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
    FavoriteQuizList(
      selectText = orderMethodSelectText,
      getChapterByIndex = getChapterByIndex,
      findByFavoriteOrderByChapterIndex = findByFavoriteOrderByChapterIndex,
      findByFavoriteOrderByFavoriteTime = findByFavoriteOrderByFavoriteTime,
      findByFavoriteOrderByWrongAnswerCount = findByFavoriteOrderByWrongAnswerCount,
      onNavigation = onNavigation,
      modifier = Modifier.weight(1f)
    )
    FavoriteTestCount(
      expand = testCountExpand,
      onExpandChange = onTestCountExpandChange,
      items = testCountItems,
      selectText = testCountSelectText,
      onSelectTextChange = onTestCountSelectTextChange,
      findQuizByFavorite = findQuizByFavorite,
      onNavigation = onNavigation,
    )
  }
}

