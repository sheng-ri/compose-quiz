package cn.hellozjf.project.composequiz.database.repository

import cn.hellozjf.project.composequiz.database.dao.QuizDao
import cn.hellozjf.project.composequiz.database.entity.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuizRepository(private val quizDao: QuizDao) {

  // TODO 后面把协程作用域改为 viewModelScope
  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  fun insertQuiz(quiz: Quiz) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.insertQuiz(quiz)
    }
  }

  fun deleteQuizByChapter(chapter: Int) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.deleteByChapter(chapter)
    }
  }

  fun findQuizFlowByChapter(chapter: Int): Flow<List<Quiz>> {
    return quizDao.findFlowByChapter(chapter)
  }

  suspend fun findQuizByChapter(chapter: Int): List<Quiz> {
    return quizDao.findByChapter(chapter)
  }

  suspend fun findQuizByFavorite(): List<Quiz> {
    return quizDao.findByFavorite()
  }

  fun findByIdListFlow(idList: List<Int>): Flow<List<Quiz>> {
    return quizDao.findByIdListFlow(idList)
  }

  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<Quiz>> {
    return quizDao.findByFavoriteOrderByChapterIndexFlow()
  }

  suspend fun findByFavoriteOrderByChapterIndex(): List<Quiz> {
    return quizDao.findByFavoriteOrderByChapterIndex()
  }

  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<Quiz>> {
    return quizDao.findByFavoriteOrderByFavoriteTimeFlow()
  }

  suspend fun findByFavoriteOrderByFavoriteTime(): List<Quiz> {
    return quizDao.findByFavoriteOrderByFavoriteTime()
  }

  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<Quiz>> {
    return quizDao.findByFavoriteOrderByWrongAnswerCountFlow()
  }

  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<Quiz> {
    return quizDao.findByFavoriteOrderByWrongAnswerCount()
  }

  fun getCount(): Int {
    return quizDao.getCount()
  }

  fun setFavorite(id: Int, favorite: Boolean, favoriteTime: Long) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.setFavorite(id, favorite, favoriteTime)
    }
  }

  suspend fun setFavoriteSuspend(id: Int, favorite: Boolean, favoriteTime: Long) {
    quizDao.setFavorite(id, favorite, favoriteTime)
  }

  fun incWrongAnswerCount(id: Int) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.incWrongAnswerCount(id)
    }
  }
}