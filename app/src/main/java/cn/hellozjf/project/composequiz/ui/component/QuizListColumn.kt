package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun QuizListColumn(
  // TODO 这里不要传 quizDTOList，传一个 (QuizKey) -> QuizDTO 的 suspend fun
  quizDTOList: List<QuizDTO>,
  quizOrder: List<Int>,
  quizSelectOption: Map<QuizKey, OptionKey>,
  onQuizSelectOptionChange: (Map<QuizKey, OptionKey>) -> Unit,
  optionOrderList: List<List<Int>>,
  title: String,
  quizKeyList: List<QuizKey>,
  onNavigation: (NavKey) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    QuizList(
      quizDTOList = quizDTOList,
      quizOrder = quizOrder,
      quizSelectedOptionMap = quizSelectOption,
      onQuizSelectedOptionChange = { mapKey, selectOption ->
        val newMap = quizSelectOption.toMutableMap()
        newMap[mapKey] = selectOption
        onQuizSelectOptionChange(newMap)
      },
      optionOrderList = optionOrderList,
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
}

@Preview(
  showBackground = true
)
@Composable
fun QuizListColumnPreview() {
  val quizDTOList: List<QuizDTO> = listOf(
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 1,
      question = "问题0",
      correctOption = "正确选项",
      wrongOption1 = "错误选项1",
      wrongOption2 = "错误选项2",
      wrongOption3 = "错误选项3",
      explanation = "问题0解释"
    ),
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 2,
      question = "问题1",
      correctOption = "正确选项",
      wrongOption1 = "错误选项1",
      wrongOption2 = "错误选项2",
      wrongOption3 = "错误选项3",
      explanation = "问题1解释"
    ),
    QuizDTO(
      chapterIndex = 0,
      quizIndex = 3,
      question = "问题2",
      correctOption = "正确选项",
      wrongOption1 = "错误选项1",
      wrongOption2 = "错误选项2",
      wrongOption3 = "错误选项3",
      explanation = "问题2解释"
    ),
  )
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
    quizDTOList = quizDTOList,
    quizOrder = quizOrder,
    quizSelectOption = quizSelectOption,
    onQuizSelectOptionChange = onQuizSelectOptionChange,
    optionOrderList = optionOrderList,
    title = "章节测试",
    quizKeyList = listOf(
      QuizKey(chapterIndex = 0, quizIndex = 1),
      QuizKey(chapterIndex = 0, quizIndex = 2),
      QuizKey(chapterIndex = 0, quizIndex = 3),
    ),
    onNavigation = {}
  )
}

