package cn.hellozjf.project.composequiz.database.repository

import cn.hellozjf.project.composequiz.database.dao.QuizDao
import cn.hellozjf.project.composequiz.database.dao.QuizZhDao
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuizZhRepository(private val quizDao: QuizZhDao) {

  // TODO 后面把协程作用域改为 viewModelScope
  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  // TODO 增删改方法变成 suspend fun
  fun insertQuiz(quiz: QuizZh) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.insertQuiz(quiz)
    }
  }

  fun deleteQuizByChapter(chapter: Int) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.deleteByChapter(chapter)
    }
  }

  fun findQuizFlowByChapter(chapter: Int): Flow<List<QuizZh>> {
    return quizDao.findFlowByChapter(chapter)
  }

  suspend fun findQuizByChapter(chapter: Int): List<QuizZh> {
    return quizDao.findByChapter(chapter)
  }

  suspend fun findQuizByFavorite(): List<QuizZh> {
    return quizDao.findByFavorite()
  }

  fun findByIdListFlow(idList: List<Int>): Flow<List<QuizZh>> {
    return quizDao.findByIdListFlow(idList)
  }

  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<QuizZh>> {
    return quizDao.findByFavoriteOrderByChapterIndexFlow()
  }

  suspend fun findByFavoriteOrderByChapterIndex(): List<QuizZh> {
    return quizDao.findByFavoriteOrderByChapterIndex()
  }

  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<QuizZh>> {
    return quizDao.findByFavoriteOrderByFavoriteTimeFlow()
  }

  suspend fun findByFavoriteOrderByFavoriteTime(): List<QuizZh> {
    return quizDao.findByFavoriteOrderByFavoriteTime()
  }

  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<QuizZh>> {
    return quizDao.findByFavoriteOrderByWrongAnswerCountFlow()
  }

  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<QuizZh> {
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