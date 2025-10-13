package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.util.TestCountConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 收藏列表 的测试行
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTestRow(
  findQuizByFavorite: suspend () -> List<Quiz>,
  onNavigation: (NavKey) -> Unit
) {

  // 下拉框里面的文本
  val testCountItems = listOf(
    TestCountConstant.FIVE,
    TestCountConstant.TEN,
    TestCountConstant.TWENTY,
    TestCountConstant.CUSTOM,
  )
  val initTestCountItem = testCountItems[0]

  // 这个是下拉框选中自定义时的搜索数量
  var customTestCount by remember { mutableStateOf(initTestCountItem.toInt()) }
  // 下拉框选中自定义时的搜索数量的上限和下限
  val minValue = 0
  val maxValue = 999

  // 跟测试数量有关的状态
  var testCountExpand by remember { mutableStateOf(false) }
  var testCountSelectText by remember { mutableStateOf(initTestCountItem) }

  // 协程作用域
  val coroutineScope = rememberCoroutineScope()

  Row(
    verticalAlignment = Alignment.CenterVertically
  ) {
    // 下拉框左边的文本
    Text("测试数量")
    // 下拉框本体
    CommonDropdown(
      expand = testCountExpand,
      onExpandChange = {
        testCountExpand = it
      },
      items = testCountItems,
      selectItem = testCountSelectText,
      onSelectItemChange = {
        testCountSelectText = it
        if (it != TestCountConstant.CUSTOM) {
          customTestCount = it.toInt()
        }
      }
    )
    // 当下拉框选择自定义时，需要显示自定义的数量
    if (testCountSelectText == TestCountConstant.CUSTOM) {
      FavoriteTestCustomCount(
        customTestCount = customTestCount,
        onCustomTestCountChange = {
          customTestCount = it
        },
        minValue = minValue,
        maxValue = maxValue
      )
    }
    // 定义空白占满剩余空间，以便把按钮定位到最右边
    Spacer(modifier = Modifier.weight(1f))
    // 最右边是个测试按钮
    Button(
      onClick = {
        coroutineScope.launch {
          val quizList = withContext(Dispatchers.IO) {
            val favoriteQuizList = findQuizByFavorite()
            val quizList = favoriteQuizList.shuffled().take(customTestCount)
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

@Preview(
  showBackground = true
)
@Composable
fun FavoriteTestRowPreview() {
  FavoriteTestRow(
    findQuizByFavorite = {
      val list = mutableListOf<Quiz>()
      for (i in 0 until 10) {
        val quiz = Quiz(
          id = i,
          chapterIndex = i,
          question = "问题$i",
          correctOption = "正确答案",
          wrongOption1 = "错误答案1",
          wrongOption2 = "错误答案2",
          wrongOption3 = "错误答案3",
          explanation = "问题${i}解释",
          favorite = true,
          favoriteTime = System.currentTimeMillis()
        )
        list.add(quiz)
      }
      list.toList()
    },
    onNavigation = {}
  )
}