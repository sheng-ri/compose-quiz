package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.repository.ChapterRepository
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import kotlinx.coroutines.flow.Flow

class ChapterViewModel(application: Application) : ViewModel() {
  private val repository: ChapterRepository

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val chapterEnDao = quizDb.chapterEnDao()
    val chapterZhDao = quizDb.chapterZhDao()
    repository = ChapterRepository(
      chapterEnDao = chapterEnDao,
      chapterZhDao = chapterZhDao
    )
  }

  suspend fun insertChapter(chapterEn: ChapterEn) {
    repository.insertChapter(chapterEn)
  }

  suspend fun insertChapter(chapterZh: ChapterZh) {
    repository.insertChapter(chapterZh)
  }

  fun findDTOFlowOrderByIndex(
    language: String
  ): Flow<List<ChapterDTO>> {
    return repository.findDTOFlowOrderByIndex(language)
  }

  fun findChapterDTOFlowByIndex(
    language: String,
    index: Int
  ): Flow<ChapterDTO?> {
    return repository.findChapterDTOFlowByIndex(
      language = language,
      index = index
    )
  }

  suspend fun findChapterDTOByIndex(
    language: String,
    index: Int
  ): ChapterDTO? {
    return repository.findChapterDTOByIndex(
      language = language,
      index = index
    )
  }

  suspend fun getCount(
    language: String
  ): Int {
    return repository.getCount(
      language = language
    )
  }

  suspend fun deleteAll() {
    repository.deleteAll()
  }
}
