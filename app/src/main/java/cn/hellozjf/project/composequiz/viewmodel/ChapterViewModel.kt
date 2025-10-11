package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.repository.ChapterRepository
import kotlinx.coroutines.flow.Flow

class ChapterViewModel(application: Application) : ViewModel() {
  private val repository: ChapterRepository

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val chapterDao = quizDb.chapterDao()
    repository = ChapterRepository(chapterDao)
  }

  fun insertChapter(chapter: Chapter) {
    repository.insertChapter(chapter)
  }

  fun findAllOrderByIndex(): Flow<List<Chapter>> {
    return repository.findAllOrderByIndex()
  }

  fun findByIndex(index: Int): Flow<List<Chapter>> {
    return repository.findByIndex(index)
  }

  fun getCount(): Int {
    return repository.getCount()
  }

  fun deleteAll() {
    repository.deleteAll()
  }
}
