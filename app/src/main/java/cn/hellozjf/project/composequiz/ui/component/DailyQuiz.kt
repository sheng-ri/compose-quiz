package cn.hellozjf.project.composequiz.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

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
    Text("${currentDate.year}年${currentDate.monthValue}月${currentDate.dayOfMonth}日")
    Text("今日未打卡")
    Button(onClick = {
      Log.d(TAG, "测试")
    }) {
      Text("开始测试")
    }
  }
}