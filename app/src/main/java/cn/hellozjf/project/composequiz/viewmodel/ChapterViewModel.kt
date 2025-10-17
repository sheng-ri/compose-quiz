package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.repository.ChapterRepository
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

  fun insertChapter(chapterEn: ChapterEn) {
    viewModelScope.launch {
      repository.insertChapter(chapterEn)
    }
  }

  fun insertChapter(chapterZh: ChapterZh) {
    viewModelScope.launch {
      repository.insertChapter(chapterZh)
    }
  }

  fun findDTOFlowOrderByIndex(
    language: String
  ): Flow<List<ChapterDTO>> {
    return repository.findDTOFlowOrderByIndex(language)
  }

  fun findDTOFlowByIndex(
    language: String,
    index: Int
  ): Flow<List<ChapterDTO>> {
    return repository.findDTOFlowByIndex(
      language = language,
      index = index
    )
  }

  suspend fun findDTOByIndex(
    language: String,
    index: Int
  ): ChapterDTO? {
    return repository.findDTOByIndex(
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
