package cn.hellozjf.project.composequiz

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.ui.screen.NavDisplayScreen
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.util.AssetUtils
import cn.hellozjf.project.composequiz.util.ChapterUtils
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.util.QuizUtils
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import cn.hellozjf.project.composequiz.viewmodel.QuizViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val TAG = "MainActivity"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ComposeQuizTheme {

        val coroutineScope = rememberCoroutineScope()
        val owner = LocalViewModelStoreOwner.current
        owner?.let {
          val quizViewModel: QuizViewModel = viewModel(
            viewModelStoreOwner = it,
            key = "QuizViewModel",
            factory = QuizViewModelFactory(
              LocalContext.current.applicationContext as Application
            )
          )
          val chapterViewModel: ChapterViewModel = viewModel(
            viewModelStoreOwner = it,
            key = "ChapterViewModel",
            factory = ChapterViewModelFactory(
              LocalContext.current.applicationContext as Application
            )
          )
          val configViewModel: ConfigViewModel = viewModel(
            viewModelStoreOwner = it,
            key = "ConfigViewModel",
            factory = ConfigViewModelFactory(
              LocalContext.current.applicationContext as Application
            )
          )
          NavDisplayScreen(
            chapterViewModel = chapterViewModel,
            quizViewModel = quizViewModel,
            configViewModel = configViewModel
          )

          // 从 CSV 中读取章节和章节题目数据，并写入数据库中
          readCsvAndWriteToDB(
            coroutineScope = coroutineScope,
            chapterViewModel = chapterViewModel,
            quizViewModel = quizViewModel
          )
        }
      }
    }
  }

  /**
   * 判断数据库中是否有数据，如果没有的话就用CSV的数据进行初始化
   */
  private fun readCsvAndWriteToDB(
    coroutineScope: CoroutineScope,
    chapterViewModel: ChapterViewModel,
    quizViewModel: QuizViewModel
  ) {
    // 读取章节信息
    coroutineScope.launch(context = Dispatchers.IO) {

      if (chapterViewModel.getCount(LanguageConstant.EN) == 0) {
        // 英文的章节表没有初始化过
        readChapterEnCsvAndWriteToDB(chapterViewModel)
      }
      if (chapterViewModel.getCount(LanguageConstant.ZH) == 0) {
        // 中文的章节表没有初始化过
        readChapterZhCsvAndWriteToDB(chapterViewModel)
      }

      if (quizViewModel.getCount(LanguageConstant.EN) == 0) {
        // 英文的题目表没有初始化过
        readQuizEnCsvAndWriteToDB(quizViewModel)
      }
      if (quizViewModel.getCount(LanguageConstant.ZH) == 0) {
        // 中文的题目表没有初始化过
        readQuizZhCsvAndWriteToDB(quizViewModel)
      }
      if (quizViewModel.getExtCount() == 0) {
        // EXT表没有初始化过
        initQuizExtAndWriteToDB(quizViewModel)
      }
    }
  }

  private suspend fun initQuizExtAndWriteToDB(
    quizViewModel: QuizViewModel
  ) {
    val quizKeyList = quizViewModel.findQuizKeyList(language = LanguageConstant.EN)
    quizViewModel.initExtList(quizKeyList)
  }

  private suspend fun readChapterEnCsvAndWriteToDB(
    chapterViewModel: ChapterViewModel
  ) {
    var chapterDTOList: List<ChapterDTO>? = null
    AssetUtils.openAndRead(
      context = this,
      path = AssetUtils.CHAPTER_EN_CSV
    ) { reader ->
      chapterDTOList = ChapterUtils.getChapterDTOListFromCsv(reader)
    }
    chapterDTOList?.let {
      for (chapterDTO in it) {
        val chapterEn = chapterDTO.toChapterEn()
        chapterViewModel.insertChapter(chapterEn)
      }
    }
  }

  private suspend fun readChapterZhCsvAndWriteToDB(
    chapterViewModel: ChapterViewModel
  ) {
    var chapterDTOList: List<ChapterDTO>? = null
    AssetUtils.openAndRead(
      context = this,
      path = AssetUtils.CHAPTER_ZH_CSV
    ) { reader ->
      chapterDTOList = ChapterUtils.getChapterDTOListFromCsv(reader)
    }
    chapterDTOList?.let {
      for (chapterDTO in it) {
        val chapterZh = chapterDTO.toChapterZh()
        chapterViewModel.insertChapter(chapterZh)
      }
    }
  }

  private suspend fun readQuizEnCsvAndWriteToDB(
    quizViewModel: QuizViewModel
  ) {
    var quizDTOList: List<QuizDTO>? = null
    AssetUtils.openAndRead(
      context = this,
      path = AssetUtils.QUIZ_EN_CSV
    ) { reader ->
      quizDTOList = QuizUtils.getQuizDTOListFromCsv(reader)
    }
    quizDTOList?.let {
      for (quizDTO in it) {
        val quizEn = quizDTO.toQuizEn()
        quizViewModel.insertQuiz(quizEn)
      }
    }
  }

  private suspend fun readQuizZhCsvAndWriteToDB(
    quizViewModel: QuizViewModel
  ) {
    var quizDTOList: List<QuizDTO>? = null
    AssetUtils.openAndRead(
      context = this,
      path = AssetUtils.QUIZ_ZH_CSV
    ) { reader ->
      quizDTOList = QuizUtils.getQuizDTOListFromCsv(reader)
    }
    quizDTOList?.let {
      for (quizDTO in it) {
        val quizZh = quizDTO.toQuizZh()
        quizViewModel.insertQuiz(quizZh)
      }
    }
  }
}

class QuizViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return QuizViewModel(application) as T
  }
}

class ChapterViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ChapterViewModel(application) as T
  }
}

class ConfigViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ConfigViewModel(application) as T
  }
}