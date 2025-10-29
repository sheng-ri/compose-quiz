package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.nav.QuizAnswerScreenKey
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.util.QuizUtils

@Composable
fun QuizListColumn(
  findQuizDTOByKey: suspend (String, QuizKey) -> QuizDTO?,
  language: String,
  quizOrder: List<Int>,
  optionOrderList: List<List<Int>>,
  quizSelectOption: Map<QuizKey, OptionKey>,
  onQuizSelectOptionChange: (Map<QuizKey, OptionKey>) -> Unit,
  title: String,
  quizKeyList: List<QuizKey>,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {
  var quizDTOList: List<QuizDTO> by remember {
    mutableStateOf(listOf())
  }
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    QuizList(
      quizDTOList = quizDTOList,
      quizSelectedOptionMap = quizSelectOption,
      onQuizSelectedOptionChange = { mapKey, selectOption ->
        val newMap = quizSelectOption.toMutableMap()
        if (newMap.containsKey(mapKey) && newMap[mapKey] == selectOption) {
          // 选择同一个选项，取消选择
          newMap.remove(mapKey)
        } else {
          newMap[mapKey] = selectOption
        }
        onQuizSelectOptionChange(newMap)
      },
      modifier = Modifier.weight(1f)
    )

    Button(
      onClick = {
        onNavigation(
          QuizAnswerScreenKey(
            title = title,
            quizKeyList = quizKeyList,
            chooseOptionMap = quizSelectOption,
            quizOrderList = quizOrder,
            optionOrderList = optionOrderList
          )
        )
      }
    ) {
      Text("提交")
    }
  }

  LaunchedEffect(
    key1 = quizKeyList.joinToString(","),
    key2 = language
  ) {
    val oldQuizDTOList = quizKeyList.mapNotNull {
      findQuizDTOByKey(language, it)
    }
    quizDTOList = QuizUtils.reorder(
      oldQuizDTOList = oldQuizDTOList,
      quizOrder = quizOrder,
      optionOrders = optionOrderList
    )
  }
}

@Preview(
  showBackground = true
)
@Composable
fun QuizListColumnPreview() {
  val quizDTOList: List<QuizDTO> = listOf(
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 0,
      question = "问题0",
      options = listOf(
        "正确选项",
        "错误选项1",
        "错误选项2",
        "错误选项3",
      ),
      correctOptionIndex = 0,
      explanation = "问题0解释"
    ),
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 1,
      question = "问题1",
      options = listOf(
        "错误选项1",
        "正确选项",
        "错误选项2",
        "错误选项3",
      ),
      correctOptionIndex = 1,
      explanation = "问题1解释"
    ),
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 2,
      question = "问题2",
      options = listOf(
        "错误选项1",
        "错误选项2",
        "正确选项",
        "错误选项3",
      ),
      correctOptionIndex = 2,
      explanation = "问题2解释"
    ),
  )
  val findQuizDTOByKey: suspend (String, QuizKey) -> QuizDTO? = { _, quizKey ->
    quizDTOList[quizKey.quizIndex]
  }
  val quizOrder: List<Int> = listOf(2, 1, 0)
  var quizSelectOption by remember {
    mutableStateOf(
      mapOf(QuizKey(0, 0) to OptionKey(0, 0, 0))
    )
  }
  val onQuizSelectOptionChange: (Map<QuizKey, OptionKey>) -> Unit = { map ->
    quizSelectOption = map
  }
  val optionOrderList: List<List<Int>> = listOf(
    listOf(0, 3, 1, 2),
    listOf(3, 1, 2, 0),
    listOf(0, 1, 3, 2),
  )
  QuizListColumn(
    findQuizDTOByKey = findQuizDTOByKey,
    language = LanguageConstant.ZH,
    quizOrder = quizOrder,
    quizSelectOption = quizSelectOption,
    onQuizSelectOptionChange = onQuizSelectOptionChange,
    optionOrderList = optionOrderList,
    title = "章节测试",
    quizKeyList = listOf(
      QuizKey(chapterIndex = 0, quizIndex = 0),
      QuizKey(chapterIndex = 0, quizIndex = 1),
      QuizKey(chapterIndex = 0, quizIndex = 2),
    ),
    onNavigation = {}
  )
}

