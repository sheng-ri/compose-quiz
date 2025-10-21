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
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.ui.screen.NavDisplayScreen
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.util.ChapterConstant
import cn.hellozjf.project.composequiz.util.ChapterQuizConstant
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.IOException

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
          val chapterQuizViewModel: ChapterQuizViewModel = viewModel(
            viewModelStoreOwner = it,
            key = "ChapterQuizViewModel",
            factory = ChapterQuizViewModelFactory(
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
            chapterQuizViewModel = chapterQuizViewModel,
            configViewModel = configViewModel
          )

          // 从 CSV 中读取章节和章节题目数据，并写入数据库中
          readCsvAndWriteToDB(
            coroutineScope = coroutineScope,
            chapterViewModel = chapterViewModel,
            chapterQuizViewModel = chapterQuizViewModel
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
    chapterQuizViewModel: ChapterQuizViewModel
  ) {
    // 读取章节信息
    coroutineScope.launch(context = Dispatchers.IO) {

      if (chapterViewModel.getCount(LanguageConstant.EN) == 0) {
        // 英文的章节表没有初始化过
        readChapterEnCsv(chapterViewModel)
      }
      if (chapterViewModel.getCount(LanguageConstant.ZH) == 0) {
        // 中文的章节表没有初始化过
        readChapterZhCsv(chapterViewModel)
      }

      if (chapterQuizViewModel.getCount(LanguageConstant.EN) == 0) {
        // 英文的题目表没有初始化过
        readQuizEnCsv(chapterQuizViewModel)
      }
      if (chapterQuizViewModel.getCount(LanguageConstant.ZH) == 0) {
        // 中文的题目表没有初始化过
        readQuizZhCsv(chapterQuizViewModel)
      }
      if (chapterQuizViewModel.getExtCount() == 0) {
        // EXT表没有初始化过
        initQuizExt(chapterQuizViewModel)
      }
    }
  }

  private suspend fun initQuizExt(
    chapterQuizViewModel: ChapterQuizViewModel
  ) {
    val quizKeyList = chapterQuizViewModel.findQuizKeyList(language = LanguageConstant.EN)
    chapterQuizViewModel.initExtList(quizKeyList)
  }

  private fun readChapterEnCsv(
    chapterViewModel: ChapterViewModel
  ) {
    try {
      this.assets.open(ChapterConstant.PATH_EN).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        for (record in csvParser) {
          val chapterEn = ChapterEn()
          chapterEn.index = record.get(ChapterConstant.INDEX).toInt()
          chapterEn.fullTitle = record.get(ChapterConstant.FULL_TITLE)
          chapterEn.simpleTitle = record.get(ChapterConstant.SIMPLE_TITLE)
          chapterEn.simpleUrl = record.get(ChapterConstant.SIMPLE_URL)
          chapterEn.fullUrl = record.get(ChapterConstant.FULL_URL)
          chapterViewModel.insertChapter(chapterEn)
        }

      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun readChapterZhCsv(
    chapterViewModel: ChapterViewModel
  ) {
    try {
      this.assets.open(ChapterConstant.PATH_ZH).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        for (record in csvParser) {
          val chapter = ChapterZh()
          chapter.index = record.get(ChapterConstant.INDEX).toInt()
          chapter.fullTitle = record.get(ChapterConstant.FULL_TITLE)
          chapter.simpleTitle = record.get(ChapterConstant.SIMPLE_TITLE)
          chapter.simpleUrl = record.get(ChapterConstant.SIMPLE_URL)
          chapter.fullUrl = record.get(ChapterConstant.FULL_URL)
          chapterViewModel.insertChapter(chapter)
        }

      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun readQuizEnCsv(
    chapterQuizViewModel: ChapterQuizViewModel
  ) {
    try {
      this.assets.open(ChapterQuizConstant.PATH_EN).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        // key 为 chapterIndex，value 为 quizIndex
        val map = mutableMapOf<Int, Int>()
        for (record in csvParser) {
          val chapterIndex = record.get(ChapterQuizConstant.CHAPTER_INDEX).toInt()
          val quizIndex = map.getOrDefault(chapterIndex, 0)
          map.put(chapterIndex, quizIndex + 1)
          val quizEn = QuizEn(
            createTime = System.currentTimeMillis(),
            chapterIndex = chapterIndex,
            quizIndex = quizIndex,
            question = record.get(ChapterQuizConstant.QUESTION),
            correctOption = record.get(ChapterQuizConstant.CORRECT_OPTION),
            wrongOption1 = record.get(ChapterQuizConstant.WRONG_OPTION1),
            wrongOption2 = record.get(ChapterQuizConstant.WRONG_OPTION2),
            wrongOption3 = record.get(ChapterQuizConstant.WRONG_OPTION3),
            explanation = record.get(ChapterQuizConstant.EXPLANATION)
          )
          chapterQuizViewModel.insertQuiz(quizEn)
        }

      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun readQuizZhCsv(
    chapterQuizViewModel: ChapterQuizViewModel
  ) {
    try {
      this.assets.open(ChapterQuizConstant.PATH_ZH).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        // key 为 chapterIndex，value 为 quizIndex
        val map = mutableMapOf<Int, Int>()
        for (record in csvParser) {
          val chapterIndex = record.get(ChapterQuizConstant.CHAPTER_INDEX).toInt()
          val quizIndex = map.getOrDefault(chapterIndex, 0)
          map.put(chapterIndex, quizIndex + 1)
          val quiz = QuizZh(
            createTime = System.currentTimeMillis(),
            chapterIndex = chapterIndex,
            quizIndex = quizIndex,
            question = record.get(ChapterQuizConstant.QUESTION),
            correctOption = record.get(ChapterQuizConstant.CORRECT_OPTION),
            wrongOption1 = record.get(ChapterQuizConstant.WRONG_OPTION1),
            wrongOption2 = record.get(ChapterQuizConstant.WRONG_OPTION2),
            wrongOption3 = record.get(ChapterQuizConstant.WRONG_OPTION3),
            explanation = record.get(ChapterQuizConstant.EXPLANATION)
          )
          chapterQuizViewModel.insertQuiz(quiz)
        }

      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
}

class ChapterQuizViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ChapterQuizViewModel(application) as T
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