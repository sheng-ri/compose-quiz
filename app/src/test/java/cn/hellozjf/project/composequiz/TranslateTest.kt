package cn.hellozjf.project.composequiz

import cn.hellozjf.project.composequiz.util.ChapterUtils
import cn.hellozjf.project.composequiz.util.QuizUtils
import cn.hellozjf.project.composequiz.util.TranslateUtils
import com.aliyun.alimt20181012.Client
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class TranslateTest {

  // 声明 Client 变量
  private lateinit var client: Client


  @Before
  fun setUp() {
    client = TranslateUtils.createClient()
  }

  @After
  fun tearDown() {
    // 好像 client 没有 close 方法，所以就不用调用了
  }

  @Test
  fun translateChapterCSV() {
    val chapterDTOList = ChapterUtils.getChapterDTOListFromCsv()
    val translatedChapterDTOList = chapterDTOList.map { chapterDTO ->
      chapterDTO.copy(
        fullTitle = TranslateUtils.en2zh(client, chapterDTO.fullTitle),
        simpleTitle = TranslateUtils.en2zh(client, chapterDTO.simpleTitle),
      )
    }
    ChapterUtils.writeChapterDTOListToCsv(
      file = File(ChapterUtils.defaultZhCsvFilePath),
      chapterDTOList = translatedChapterDTOList
    )
  }

  @Test
  fun translateQuizCSV() {
    val quizDTOEnList = QuizUtils.getQuizDTOListFromCsv(
      file = File(QuizUtils.defaultEnCsvFilePath)
    )
    val quizDTOZhList = QuizUtils.getQuizDTOListFromCsv(
      file = File(QuizUtils.defaultZhCsvFilePath)
    )
    val skipQuizKeyList = quizDTOZhList.map { it.getQuizKey() }
    val translatedQuizDTOList = quizDTOEnList.mapNotNull { quizDTO ->
      val quizKey = quizDTO.getQuizKey()
      if (skipQuizKeyList.contains(quizKey)) {
        // 这题已经翻译过了，不用再翻译了
        null
      } else {
        println("正在翻译 第${quizDTO.chapterIndex}章 第${quizDTO.quizIndex}题")
        quizDTO.copy(
          question = TranslateUtils.en2zh(client, quizDTO.question),
          options = quizDTO.options.map { TranslateUtils.en2zh(client, it) },
          explanation = TranslateUtils.en2zh(client, quizDTO.explanation)
        )
      }
    }

    val quizDTOList = quizDTOZhList + translatedQuizDTOList
    val sortedQuizDTOList = quizDTOList.sortedWith(
      compareBy(
        { it.chapterIndex },
        { it.quizIndex }
      )
    )
    QuizUtils.writeQuizDTOListToCsv(
      file = File(QuizUtils.defaultZhCsvFilePath),
      quizDTOList = sortedQuizDTOList
    )
  }

}