package cn.hellozjf.project.composequiz.ui.component

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * 这是每日测试 组件
 */

private val TAG = "DailyQuiz"

@Composable
fun DailyQuiz(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  contentDescription: String
) {
  // 今天是 xxxx年xx月xx日，今天未打卡
  val currentDate by remember {
    mutableStateOf<LocalDate>(LocalDate.now())
  }

  // 开始今日测试
  // 以 yyyyMMdd 为种子，去数据库随机抽取20题，然后打开做题页面
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Text("${currentDate.year}年${currentDate.monthValue}月${currentDate.dayOfMonth}日")
    // TODO
//    PunchCalendar()
    Button(onClick = {
      Log.d(TAG, "测试")
    }) {
      Text("开始测试")
    }
  }
}

@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_NO,
  name = "浅色模式"
)
@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_YES,
  name = "深色模式"
)
@Composable
fun PunchCalendarPreview() {
  val currentMonth = remember { YearMonth.now() }
  var punchRecords by remember {
    mutableStateOf(
      setOf<LocalDate>(
        LocalDate.of(2025, 11, 17)
      )
    )
  }
  ComposeQuizTheme {
    Surface {
      PunchCalendar(
        currentMonth = currentMonth,
        punchRecords = punchRecords,
        onMonthChange = { yearMonth ->
          Log.d(TAG, "onMonthChange: $yearMonth")
        }
      )
    }
  }
}

@Composable
fun PunchCalendar(
  currentMonth: YearMonth,
  punchRecords: Set<LocalDate>,
  onMonthChange: (YearMonth) -> Unit,
  modifier: Modifier = Modifier,
) {
//  // 状态管理：当前显示的月份和打卡记录
//  val currentMonth = remember { YearMonth.now() }
//  var punchRecords by remember { mutableStateOf(setOf<LocalDate>()) }

  // 获取当前月份需要显示的所有日期（包括前后补足的空格）
  val dates = remember(currentMonth) {
    generateCalendarDates(currentMonth)
  }

  // 用于累计水平拖动距离
  var dragAmount by remember { mutableFloatStateOf(0f) }

  // 构建日历UI
  Column(
    modifier = modifier.pointerInput(Unit) {
      // 修正拼写错误
      detectHorizontalDragGestures(
        onDragEnd = {
          // 当拖动结束时判断方向
          // 向右拖动（dragAmount为正），显示上一个月
          if (dragAmount > 50) { // 阈值设为50像素
            onMonthChange(currentMonth.minusMonths(1))
          } else if (dragAmount < -50) { // 向左拖动（dragAmount为负），显示下一个月
            onMonthChange(currentMonth.plusMonths(1))
          }
          // 重置拖动距离
          dragAmount = 0f
        },
        onHorizontalDrag = { change, drag ->
          // 实时累加拖动距离
          // drag > 0 表示向右拖动, drag < 0 表示向左拖动
          dragAmount += drag
          // 消费掉事件，防止父组件也响应拖动
          change.consume()
        }
      )
    },
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "${currentMonth.year}年${currentMonth.monthValue}月",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .padding(16.dp),
    )
    LazyVerticalGrid(
      columns = GridCells.Fixed(7), // 固定7列，代表一周7天
      modifier = Modifier.padding(horizontal = 16.dp)
    ) {
      items(dates) { date ->
        CalendarDay(
          date = date,
          isPunched = punchRecords.contains(date),
        )
      }
    }
  }
}

/**
 * 生成一个月的日期列表，并用null补足前后空格，确保每周7天。
 */
private fun generateCalendarDates(month: YearMonth): List<LocalDate> {
  val firstDayOfMonth = month.atDay(1)
  // 计算本月第一天是星期几（ISO标准，周一=1, 周日=7）
  val dayOfWeekValue = firstDayOfMonth.dayOfWeek.value
  // 计算在第一个日期之前需要补足多少天（本例中从周一开始显示一周）
  val daysBefore = (dayOfWeekValue - DayOfWeek.MONDAY.value + 7) % 7

  val startDate = firstDayOfMonth.minusDays(daysBefore.toLong())
  val totalDays = 42 // 通常补足至6行，共42天，以保持日历外观整齐

  return List(totalDays) { index ->
    startDate.plusDays(index.toLong())
  }
}

/**
 * 单个日期单元格的Composable。
 */
@Composable
fun CalendarDay(
  date: LocalDate,
  isPunched: Boolean,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .aspectRatio(1f) // 确保每个单元格是正方形
      .padding(4.dp)
      .clip(CircleShape)
      .background(color = if (isPunched) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
      .padding(4.dp),
  ) {
    // 根据是否打卡，显示不同的内容
    if (isPunched) {
      // 打卡状态：显示一个带勾的圈
      PunchIcon(
        imageVector = Icons.Default.Check
      )
    } else {
      // 未打卡状态：显示日期数字
      Text(
        text = date.dayOfMonth.toString(),
        color = if (date.month == YearMonth.now().month) {
          // 当前月份的日期用主色
          MaterialTheme.colorScheme.onBackground
        } else {
          // 非当前月份的日期透明不显示
          MaterialTheme.colorScheme.onSurface.copy(alpha = 0f)
        }
      )
    }
  }
}

/**
 * 打卡图标的Composable。
 */
@Composable
fun PunchIcon(
  imageVector: ImageVector
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .size(32.dp)
      .clip(CircleShape)
      .background(MaterialTheme.colorScheme.primary)
  ) {
    Icon(
      imageVector = imageVector,
      contentDescription = "已打卡",
      tint = MaterialTheme.colorScheme.onPrimary,
      modifier = Modifier.size(24.dp)
    )
  }
}