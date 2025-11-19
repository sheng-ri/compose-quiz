package cn.hellozjf.project.composequiz.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cn.hellozjf.project.composequiz.ui.component.PunchCalendar
import cn.hellozjf.project.composequiz.viewmodel.PunchViewModel
import java.time.LocalDate
import java.time.YearMonth

private val TAG = "PunchScreen"

/**
 * 打卡屏幕
 */
@Composable
fun PunchScreen(
  punchViewModel: PunchViewModel
) {

  // 把当前的年月取出来
  var currentMonth by remember { mutableStateOf(YearMonth.now()) }
  // 根据当前的年月，把punch记录取出来
  val punchList by punchViewModel.getByYearMonthFlow(currentMonth.year, currentMonth.monthValue)
    .collectAsState(listOf())

  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier.padding(innerPadding)
    ) {
      PunchCalendar(
        currentMonth = currentMonth,
        punchRecords = punchList.map { LocalDate.of(it.year, it.month, it.day) }.toSet(),
        onMonthChange = { yearMonth ->
          Log.d(TAG, "onMonthChange: $yearMonth")
          currentMonth = yearMonth
        }
      )
    }
  }
}