package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.database.repository.QuizRepository
import cn.hellozjf.project.composequiz.database.repository.QuizZhRepository
import kotlinx.coroutines.flow.Flow

class ChapterQuizZhViewModel(application: Application) : ViewModel() {
  private val repository: QuizZhRepository

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val quizDao = quizDb.quizZhDao()
    repository = QuizZhRepository(quizDao)
  }

  fun insertQuiz(quiz: QuizZh) {
    repository.insertQuiz(quiz)
  }

  fun findQuizFlowByChapter(chapter: Int): Flow<List<QuizZh>> {
    return repository.findQuizFlowByChapter(chapter)
  }

  suspend fun findQuizByChapterIndex(chapter: Int): List<QuizZh> {
    return repository.findQuizByChapter(chapter)
  }

  suspend fun findQuizByFavorite(): List<QuizZh> {
    return repository.findQuizByFavorite()
  }

  fun findByIdList(idList: List<Int>): Flow<List<QuizZh>> {
    return repository.findByIdListFlow(idList)
  }

  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<QuizZh>> {
    return repository.findByFavoriteOrderByChapterIndexFlow()
  }

  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<QuizZh>> {
    return repository.findByFavoriteOrderByFavoriteTimeFlow()
  }

  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<QuizZh>> {
    return repository.findByFavoriteOrderByWrongAnswerCountFlow()
  }

  suspend fun findByFavoriteOrderByChapterIndex(): List<QuizZh> {
    return repository.findByFavoriteOrderByChapterIndex()
  }

  suspend fun findByFavoriteOrderByFavoriteTime(): List<QuizZh> {
    return repository.findByFavoriteOrderByFavoriteTime()
  }

  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<QuizZh> {
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

  suspend fun setFavoriteSuspend(id: Int, favorite: Boolean, favoriteTime: Long) {
    repository.setFavoriteSuspend(id, favorite, favoriteTime)
  }

  fun incWrongAnswerCount(id: Int) {
    repository.incWrongAnswerCount(id)
  }
}