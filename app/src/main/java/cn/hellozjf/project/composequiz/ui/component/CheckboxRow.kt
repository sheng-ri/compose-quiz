package cn.hellozjf.project.composequiz.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.ui.theme.ErrorText40
import cn.hellozjf.project.composequiz.ui.theme.ErrorText80
import cn.hellozjf.project.composequiz.ui.theme.NormalText40
import cn.hellozjf.project.composequiz.ui.theme.NormalText80
import cn.hellozjf.project.composequiz.ui.theme.SuccessText40
import cn.hellozjf.project.composequiz.ui.theme.SuccessText80

/**
 * 复选框和文本
 */
@Composable
fun CheckboxRow(
  option: String,                                     // 当前复选框的文本
  optionKey: OptionKey,                               // 当前复选框key
  selectedOptionKey: OptionKey?,                      // 选中复选框key，如果当前复选框key==选中复选框key，则代表当前复选框被选中
  modifier: Modifier = Modifier,                      // 方便外部传递样式参数
  correctOptionKey: OptionKey? = null,                // 正确复选框key
  showColor: Boolean = false,                         // 是否显示颜色，否一律显示黑色；是正确选项显示绿色，错误选中选项显示红色，其它显示黑色
  enabled: Boolean = true,                             // 是否可以点击复选框
  onSelectedOptionKeyChange: (OptionKey) -> Unit = {}     // 变更选中复选框key
) {

  val isDarkMode = isSystemInDarkTheme()
  val normalTextColor = if (isDarkMode) NormalText40 else NormalText80
  val successTextColor = if (isDarkMode) SuccessText40 else SuccessText80
  val errorTextColor = if (isDarkMode) ErrorText40 else ErrorText80

  Row(
    modifier = modifier
      .fillMaxWidth()
      .then(
        if (enabled) {
          Modifier.clickable { onSelectedOptionKeyChange(optionKey) }
        } else {
          Modifier
        }
      ),
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.Start
  ) {
    Checkbox(
      checked = selectedOptionKey == optionKey,
      onCheckedChange = null
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = option,
      color = if (showColor) {
        when (optionKey) {
          correctOptionKey -> successTextColor
          selectedOptionKey -> errorTextColor
          else -> normalTextColor
        }
      } else {
        normalTextColor
      }
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
fun CheckboxRowPreview() {
  val optionKey = OptionKey(
    chapterIndex = 0,
    quizIndex = 0,
    optionIndex = 0
  )
  var selectOptionKey: OptionKey? by remember {
    mutableStateOf(null)
  }
  CheckboxRow(
    option = "错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案错误答案",
    optionKey = optionKey,
    selectedOptionKey = selectOptionKey,
    onSelectedOptionKeyChange = {
      selectOptionKey = if (selectOptionKey != it) {
        it
      } else {
        null
      }
    }
  )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "浅色模式")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "深色模式")
@Composable
fun CheckboxRowListPreview() {
  val chapterIndex = 0
  val quizIndex = 0
  var selectedOptionKey by remember {
    mutableStateOf<OptionKey?>(null)
  }

  ComposeQuizTheme {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxWidth()
      ) {
        for (i in 0 until 4) {
          CheckboxRow(
            option = "选项$i",
            optionKey = OptionKey(
              chapterIndex = chapterIndex,
              quizIndex = quizIndex,
              optionIndex = i
            ),
            selectedOptionKey = selectedOptionKey,
            onSelectedOptionKeyChange = { newSelectedOptionKey ->
              selectedOptionKey = if (selectedOptionKey != newSelectedOptionKey) {
                newSelectedOptionKey
              } else {
                null
              }
            },
            correctOptionKey = OptionKey(
              chapterIndex = chapterIndex,
              quizIndex = quizIndex,
              optionIndex = 0
            ),
            showColor = true
          )
        }
      }
    }
  }
}