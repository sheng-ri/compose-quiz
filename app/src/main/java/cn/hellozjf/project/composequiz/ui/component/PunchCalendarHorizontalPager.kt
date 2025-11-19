package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.database.entity.Punch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun PunchCalendarHorizontalPager(
  currentMonth: YearMonth,
  getByYearMonth: suspend (Int, Int) -> List<Punch>
) {

  // 定义一个无限大翻页
  val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }

  HorizontalPager(
    state = pagerState,
    modifier = Modifier.fillMaxWidth()
  ) { page ->

    val yearMonth = currentMonth.plusMonths((page - Int.MAX_VALUE / 2).toLong())
    var punchList by remember { mutableStateOf<List<Punch>>(listOf()) }

    LaunchedEffect(key1 = yearMonth) {
      punchList = getByYearMonth(yearMonth.year, yearMonth.monthValue)
    }

    PunchCalendar(
      currentMonth = yearMonth,
      punchRecords = punchList.map { LocalDate.of(it.year, it.month, it.day) }.toSet(),
    )
  }
}

@Composable
fun PunchCalendar(
  currentMonth: YearMonth,
  punchRecords: Set<LocalDate>,
  modifier: Modifier = Modifier,
) {

  // 获取当前月份需要显示的所有日期（包括前后补足的空格）
  val dates = remember(currentMonth) {
    generateCalendarDates(currentMonth)
  }

  // 构建日历UI
  Column(
    modifier = modifier,
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
          currentMonth = currentMonth,
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
  currentMonth: YearMonth,
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
        color = if (date.month == currentMonth.month) {
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