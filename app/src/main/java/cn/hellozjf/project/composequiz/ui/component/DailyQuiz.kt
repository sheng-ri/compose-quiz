package cn.hellozjf.project.composequiz.ui.component

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.database.entity.Punch
import cn.hellozjf.project.composequiz.database.entity.QuizExt
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.nav.QuizScreenKey
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.util.LanguageConstant
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import kotlin.random.Random

/**
 * 这是每日测试 组件
 */

private val TAG = "DailyQuiz"

@Composable
fun DailyQuiz(
  language: String,
  getByYearMonth: suspend (Int, Int) -> List<Punch>,
  exists: suspend (Int, Int, Int) -> Boolean,
  findAllQuizExt: suspend () -> List<QuizExt>,
  insertPunch: suspend (Punch) -> Unit,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
) {

  var enabled by remember { mutableStateOf(true) }
  val coroutineScope = rememberCoroutineScope()
  val now by remember { mutableStateOf(LocalDate.now()) }

  LaunchedEffect(key1 = Unit) {
    enabled = !exists(now.year, now.monthValue, now.dayOfMonth)
  }

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
    Button(
      onClick = {
        Log.d(TAG, "测试")
        // 这里要跳转到测试页面
        // 使用年月日做为种子，从 quiz 表随机挑选20题（我统计过每章平均问题是18.684题）
        coroutineScope.launch {
          // 从 quiz_ext 表中随机挑选20题
          val quizExtList = findAllQuizExt()
          val seededRandom =
            Random(seed = 1L * (now.year * 10000 + now.monthValue * 100 + now.dayOfMonth))
          val quizKeyList = mutableListOf<QuizKey>()
          for (i in 0 until 20) {
            val index = seededRandom.nextInt(quizExtList.size)
            val quizExt = quizExtList[index]
            quizKeyList.add(
              QuizKey(
                chapterIndex = quizExt.chapterIndex,
                quizIndex = quizExt.quizIndex
              )
            )
          }
          // 跳转 QuizScreen
          onNavigation(
            QuizScreenKey(
            titleEn = "${now.year}-${now.monthValue}-${now.dayOfMonth} Test",
            titleZh = "${now.year}-${now.monthValue}-${now.dayOfMonth} 测试",
            quizKeyList = quizKeyList,
            onAnswerAllCorrect = {
              coroutineScope.launch {
                // 往 punch 表添加记录
                insertPunch(
                  Punch(
                    year = now.year,
                    month = now.monthValue,
                    day = now.dayOfMonth
                  )
                )
              }
            }
          ))
        }
      },
      enabled = enabled
    ) {
      if (enabled) {
        Text("开始测试")
      } else {
        Text("今日测试已完成")
      }
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
        exists = { year, month, day ->
          true
        },
        findAllQuizExt = {
          listOf()
        },
        insertPunch = { _ -> },
        onNavigation = {}
      )
    }
  }
}

