package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.repository.ChapterRepository

class ChapterViewModel(application: Application) : ViewModel() {
  private val repository: ChapterRepository
  val searchResults: MutableLiveData<List<Chapter>>

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val chapterDao = quizDb.chapterDao()
    repository = ChapterRepository(chapterDao)

    searchResults = repository.searchResults
  }

  fun insertChapter(chapter: Chapter) {
    repository.insertChapter(chapter)
  }

  fun findAllOrderByIndex() {
    repository.findAllOrderByIndex()
  }

  fun getCount(): Int {
    return repository.getCount()
  }

  fun deleteAll() {
    repository.deleteAll()
  }
}
