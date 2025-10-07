package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.database.repository.QuizRepository

class ChapterQuizViewModel(application: Application) : ViewModel() {
  private val repository: QuizRepository
  val searchResults: MutableLiveData<List<Quiz>>

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val quizDao = quizDb.quizDao()
    repository = QuizRepository(quizDao)

    searchResults = repository.searchResults
  }

  fun insertQuiz(quiz: Quiz) {
    repository.insertQuiz(quiz)
  }

  fun findQuizByChapter(chapter: Int) {
    repository.findQuizByChapter(chapter)
  }

  fun deleteQuizByChapter(chapter: Int) {
    repository.deleteQuizByChapter(chapter)
  }
}