package cn.hellozjf.project.composequiz.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.dto.OptionKey
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import kotlin.random.Random

class QuizScreenViewModel : ViewModel() {

  // 这是问题列表，初始为空列表，当 LaunchedEffect 执行完毕之后，就能得到实际的问题列表了
  var quizDTOList by mutableStateOf<List<QuizDTO>>(listOf())

  var seed by mutableLongStateOf(0L)

  val random by derivedStateOf {
    Random(seed)
  }

  // 这是题目的顺序
  val quizOrder by derivedStateOf {
    List(quizDTOList.size) {
      it
    }.shuffled(random)
  }

  // 这是各个题目选项的顺序
  val optionOrderList by derivedStateOf {
    List(quizDTOList.size) {
      List(4) { it }.shuffled(random)
    }
  }

  // 问题ID选择的答案
  var quizSelectOption = mutableStateMapOf<QuizKey, OptionKey>()

  var showMenu by mutableStateOf(false)
}