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
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.ui.screen.NavDisplayScreen
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.util.ChapterConstant
import cn.hellozjf.project.composequiz.util.ChapterQuizConstant
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
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
          NavDisplayScreen(
            chapterViewModel = chapterViewModel,
            chapterQuizViewModel = chapterQuizViewModel
          )

          readCsvAndWriteToDB(coroutineScope, chapterViewModel, chapterQuizViewModel)
        }
      }
    }
  }

  private fun readCsvAndWriteToDB(
    coroutineScope: CoroutineScope,
    chapterViewModel: ChapterViewModel,
    chapterQuizViewModel: ChapterQuizViewModel
  ) {
    // 读取章节信息
    coroutineScope.launch(context = Dispatchers.IO) {
      if (chapterViewModel.getCount() == 0) {
        readChapterCsv(chapterViewModel)
      }
      if (chapterQuizViewModel.getCount() == 0) {
        readQuizCsv(chapterQuizViewModel)
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

  private fun readQuizCsv(
    chapterQuizViewModel: ChapterQuizViewModel
  ) {
    try {
      this.assets.open(ChapterQuizConstant.PATH).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        for (record in csvParser) {
          val quiz = Quiz(
            chapterIndex = record.get(ChapterQuizConstant.CHAPTER_INDEX).toInt(),
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