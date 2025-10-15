package cn.hellozjf.project.composequiz.database.repository

import cn.hellozjf.project.composequiz.database.dao.QuizEnDao
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuizEnRepository(private val quizEnDao: QuizEnDao) {

  // TODO 后面把协程作用域改为 viewModelScope
  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  fun insertQuiz(quizEn: QuizEn) {
    coroutineScope.launch(Dispatchers.IO) {
      quizEnDao.insertQuiz(quizEn)
    }
  }

  fun deleteQuizByChapter(chapter: Int) {
    coroutineScope.launch(Dispatchers.IO) {
      quizEnDao.deleteByChapter(chapter)
    }
  }

  fun findQuizFlowByChapter(chapter: Int): Flow<List<QuizEn>> {
    return quizEnDao.findFlowByChapterIndex(chapter)
  }

  suspend fun findQuizByChapter(chapter: Int): List<QuizEn> {
    return quizEnDao.findByChapterIndex(chapter)
  }

  suspend fun findQuizByFavorite(): List<QuizEn> {
    return quizEnDao.findByFavorite()
  }

  fun findByIdListFlow(idList: List<Int>): Flow<List<QuizEn>> {
    return quizEnDao.findByIdListFlow(idList)
  }

  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<QuizEn>> {
    return quizEnDao.findByFavoriteOrderByChapterIndexFlow()
  }

  suspend fun findByFavoriteOrderByChapterIndex(): List<QuizEn> {
    return quizEnDao.findByFavoriteOrderByChapterIndex()
  }

  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<QuizEn>> {
    return quizEnDao.findByFavoriteOrderByFavoriteTimeFlow()
  }

  suspend fun findByFavoriteOrderByFavoriteTime(): List<QuizEn> {
    return quizEnDao.findByFavoriteOrderByFavoriteTime()
  }

  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<QuizEn>> {
    return quizEnDao.findByFavoriteOrderByWrongAnswerCountFlow()
  }

  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<QuizEn> {
    return quizEnDao.findByFavoriteOrderByWrongAnswerCount()
  }

  fun getCount(): Int {
    return quizEnDao.getCount()
  }

  fun setFavorite(id: Int, favorite: Boolean, favoriteTime: Long) {
    coroutineScope.launch(Dispatchers.IO) {
      quizEnDao.setFavorite(id, favorite, favoriteTime)
    }
  }

  suspend fun setFavoriteSuspend(id: Int, favorite: Boolean, favoriteTime: Long) {
    quizEnDao.setFavorite(id, favorite, favoriteTime)
  }

  fun incWrongAnswerCount(id: Int) {
    coroutineScope.launch(Dispatchers.IO) {
      quizEnDao.incWrongAnswerCount(id)
    }
  }

  suspend fun findByChapterIndexAndQuizIndex(chapterIndex: Int, quizIndex: Int): QuizEn? {
    return quizEnDao.findByChapterIndexAndQuizIndex(
      chapterIndex = chapterIndex,
      quizIndex = quizIndex
    )
  }
}