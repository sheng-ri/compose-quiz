package cn.hellozjf.project.composequiz.ui.component

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Punch
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.util.LanguageConstant
import org.apache.fontbox.ttf.model.Language
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * 这是每日测试 组件
 */

private val TAG = "DailyQuiz"

@Composable
fun DailyQuiz(
  language: String,
  getByYearMonth: suspend (Int, Int) -> List<Punch>,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
) {
  // 开始今日测试
  // 以 yyyyMMdd 为种子，去数据库随机抽取20题，然后打开做题页面
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    PunchCalendarHorizontalPager(
      currentMonth = YearMonth.now(),
      getByYearMonth = getByYearMonth
    )
    Button(onClick = {
      Log.d(TAG, "测试")
      // TODO 这里要跳转到测试页面
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
  ComposeQuizTheme {
    Surface {
      DailyQuiz(
        language = LanguageConstant.EN,
        getByYearMonth = { year, month ->
          if (year == 2025) {
            when (month) {
              10 -> {
                listOf(Punch(2025, 10, 1))
              }

              11 -> {
                listOf(Punch(2025, 11, 19), Punch(2025, 11, 20))
              }

              else -> {
                listOf()
              }
            }
          } else {
            listOf()
          }
        },
        onNavigation = {}
      )
    }
  }
}

