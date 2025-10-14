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
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.ui.screen.NavDisplayScreen
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.util.ChapterConstant
import cn.hellozjf.project.composequiz.util.ChapterQuizConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizZhViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterZhViewModel
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
          val chapterQuizZhViewModel: ChapterQuizZhViewModel = viewModel(
            viewModelStoreOwner = it,
            key = "ChapterQuizZhViewModel",
            factory = ChapterQuizZhViewModelFactory(
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
          val chapterZhViewModel: ChapterZhViewModel = viewModel(
            viewModelStoreOwner = it,
            key = "ChapterZhViewModel",
            factory = ChapterZhViewModelFactory(
              LocalContext.current.applicationContext as Application
            )
          )
          NavDisplayScreen(
            chapterViewModel = chapterViewModel,
            chapterZhViewModel = chapterZhViewModel,
            chapterQuizViewModel = chapterQuizViewModel,
            chapterQuizZhViewModel = chapterQuizZhViewModel
          )

          readCsvAndWriteToDB(
            coroutineScope = coroutineScope,
            chapterViewModel = chapterViewModel,
            chapterZhViewModel = chapterZhViewModel,
            chapterQuizViewModel = chapterQuizViewModel,
            chapterQuizZhViewModel = chapterQuizZhViewModel
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
    chapterZhViewModel: ChapterZhViewModel,
    chapterQuizViewModel: ChapterQuizViewModel,
    chapterQuizZhViewModel: ChapterQuizZhViewModel,
  ) {
    // 读取章节信息
    coroutineScope.launch(context = Dispatchers.IO) {
      if (chapterViewModel.getCount() == 0) {
        readChapterCsv(chapterViewModel)
      }
      if (chapterZhViewModel.getCount() == 0) {
        readChapterZhCsv(chapterZhViewModel)
      }
      if (chapterQuizViewModel.getCount() == 0) {
        readQuizCsv(chapterQuizViewModel)
      }
      if (chapterQuizZhViewModel.getCount() == 0) {
        readQuizZhCsv(chapterQuizZhViewModel)
      }
    }
  }

  private fun readChapterCsv(
    chapterViewModel: ChapterViewModel
  ) {
    try {
      this.assets.open(ChapterConstant.PATH).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        for (record in csvParser) {
          val chapter = Chapter()
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

  private fun readChapterZhCsv(
    chapterViewModel: ChapterZhViewModel
  ) {
    try {
      this.assets.open(ChapterConstant.PATH).bufferedReader().use { reader ->
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

  private fun readQuizCsv(
    chapterQuizViewModel: ChapterQuizViewModel
  ) {
    try {
      this.assets.open(ChapterQuizConstant.PATH).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        // key 为 chapterIndex，value 为 quizIndex
        val map = mutableMapOf<Int, Int>()
        for (record in csvParser) {
          val chapterIndex = record.get(ChapterQuizConstant.CHAPTER_INDEX).toInt()
          val quizIndex = map.getOrDefault(chapterIndex, 0) + 1
          map.put(chapterIndex, quizIndex)
          val quiz = Quiz(
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

  private fun readQuizZhCsv(
    chapterQuizViewModel: ChapterQuizZhViewModel
  ) {
    try {
      this.assets.open(ChapterQuizConstant.PATH).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        // key 为 chapterIndex，value 为 quizIndex
        val map = mutableMapOf<Int, Int>()
        for (record in csvParser) {
          val chapterIndex = record.get(ChapterQuizConstant.CHAPTER_INDEX).toInt()
          val quizIndex = map.getOrDefault(chapterIndex, 0) + 1
          map.put(chapterIndex, quizIndex)
          val quiz = QuizZh(
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

class ChapterQuizZhViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ChapterQuizZhViewModel(application) as T
  }
}

class ChapterViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ChapterViewModel(application) as T
  }
}

class ChapterZhViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ChapterZhViewModel(application) as T
  }
}