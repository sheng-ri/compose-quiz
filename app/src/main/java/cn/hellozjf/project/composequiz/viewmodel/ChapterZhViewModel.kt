package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.repository.ChapterRepository
import cn.hellozjf.project.composequiz.database.repository.ChapterZhRepository
import kotlinx.coroutines.flow.Flow

class ChapterZhViewModel(application: Application) : ViewModel() {
  private val repository: ChapterZhRepository

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val chapterDao = quizDb.chapterZhDao()
    repository = ChapterZhRepository(chapterDao)
  }

  fun insertChapter(chapter: ChapterZh) {
    repository.insertChapter(chapter)
  }

  fun findAllOrderByIndex(): Flow<List<ChapterZh>> {
    return repository.findAllOrderByIndex()
  }

  fun findByIndexFlow(index: Int): Flow<List<ChapterZh>> {
    return repository.findByIndexFlow(index)
  }

  suspend fun findByIndex(index: Int): ChapterZh? {
    return repository.findByIndex(index)
  }

  fun getCount(): Int {
    return repository.getCount()
  }

  fun deleteAll() {
    repository.deleteAll()
  }
}
