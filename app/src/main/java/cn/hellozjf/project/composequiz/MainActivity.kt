package cn.hellozjf.project.composequiz

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import cn.hellozjf.project.composequiz.ui.screen.NavDisplayScreen
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.util.AssetUtils
import cn.hellozjf.project.composequiz.util.ChapterUtils
import cn.hellozjf.project.composequiz.util.LanguageConstant
import cn.hellozjf.project.composequiz.util.QuizConstant
import cn.hellozjf.project.composequiz.util.QuizUtils
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.ConfigViewModel
import cn.hellozjf.project.composequiz.viewmodel.PunchViewModel
import cn.hellozjf.project.composequiz.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val TAG = "MainActivity"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ComposeQuizTheme {

        Log.d(TAG, "MainActivity setContent")

        val coroutineScope = rememberCoroutineScope()
        val owner = LocalViewModelStoreOwner.current
        owner?.let {
          val quizViewModel: QuizViewModel = hiltViewModel()
          val chapterViewModel: ChapterViewModel = hiltViewModel()
          val configViewModel: ConfigViewModel = hiltViewModel()
          val punchViewModel: PunchViewModel = hiltViewModel()
          NavDisplayScreen(
            chapterViewModel = chapterViewModel,
            quizViewModel = quizViewModel,
            configViewModel = configViewModel,
            punchViewModel = punchViewModel
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
    }
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

  /**
   * 初始化英文版本的题目，同时初始化问题的描述信息
   */
  private suspend fun readQuizEnCsvAndWriteToDB(
    quizViewModel: QuizViewModel
  ) {
    // 首先从 EXT 文件中读取描述信息
    val quizKeyDescriptionMap = mutableMapOf<QuizKey, String>()
    AssetUtils.openAndRead(
      context = this,
      path = AssetUtils.QUIZ_EXT_CSV
    ) { reader ->
      CSVFormat.DEFAULT.parse(reader).use { csvParser ->
        for (record in csvParser) {
          val chapterIndex = record.get(QuizConstant.CHAPTER_INDEX).toInt()
          val quizIndex = record.get(QuizConstant.QUIZ_INDEX).toInt()
          val description = record.get(QuizConstant.DESCRIPTION)
          quizKeyDescriptionMap.put(QuizKey(chapterIndex, quizIndex), description)
        }
      }
    }
    // 然后读取 CSV 文件，拼上刚才获取的描述信息，得到 quizDTOList
    var quizDTOList: List<QuizDTO>? = null
    AssetUtils.openAndRead(
      context = this,
      path = AssetUtils.QUIZ_EN_CSV
    ) { reader ->
      quizDTOList = QuizUtils.getQuizDTOListFromCsv(
        reader = reader,
        quizKeyDescriptionMap = quizKeyDescriptionMap
      )
    }
    quizDTOList?.let {
      for (quizDTO in it) {
        // 首先存储问题的英文版本内容
        val quizEn = quizDTO.toQuizEn()
        quizViewModel.insertQuiz(quizEn)
        // 然后存储问题的描述信息
        val quizExt = quizDTO.toQuizExt()
        quizViewModel.insertQuizExt(quizExt)
      }
    }
  }

  /**
   * 初始化中文版本的题目
   */
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