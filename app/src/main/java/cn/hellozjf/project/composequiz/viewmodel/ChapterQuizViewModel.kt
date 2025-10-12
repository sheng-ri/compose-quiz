package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.database.repository.QuizRepository
import kotlinx.coroutines.flow.Flow

class ChapterQuizViewModel(application: Application) : ViewModel() {
  private val repository: QuizRepository

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val quizDao = quizDb.quizDao()
    repository = QuizRepository(quizDao)
  }

  fun insertQuiz(quiz: Quiz) {
    repository.insertQuiz(quiz)
  }

  fun findQuizFlowByChapter(chapter: Int): Flow<List<Quiz>> {
    return repository.findQuizFlowByChapter(chapter)
  }

  suspend fun findQuizByChapter(chapter: Int): List<Quiz> {
    return repository.findQuizByChapter(chapter)
  }

  suspend fun findQuizByFavorite(): List<Quiz> {
    return repository.findQuizByFavorite()
  }

  fun findByIdList(idList: List<Int>): Flow<List<Quiz>> {
    return repository.findByIdListFlow(idList)
  }

  fun findByFavoriteOrderByChapterIndex(): Flow<List<Quiz>> {
    return repository.findByFavoriteOrderByChapterIndex()
  }

  fun findByFavoriteOrderByFavoriteTime(): Flow<List<Quiz>> {
    return repository.findByFavoriteOrderByFavoriteTime()
  }

  fun findByFavoriteOrderByWrongAnswerCount(): Flow<List<Quiz>> {
    return repository.findByFavoriteOrderByWrongAnswerCount()
  }

  fun deleteQuizByChapter(chapter: Int) {
    repository.deleteQuizByChapter(chapter)
  }

  fun getCount(): Int {
    return repository.getCount()
  }

  fun setFavorite(id: Int, favorite: Boolean, favoriteTime: Long) {
    repository.setFavorite(id, favorite, favoriteTime)
  }

  fun incWrongAnswerCount(id: Int) {
    repository.incWrongAnswerCount(id)
  }
}