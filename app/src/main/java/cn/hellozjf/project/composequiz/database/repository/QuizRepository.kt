package cn.hellozjf.project.composequiz.database.repository

import androidx.room.Query
import cn.hellozjf.project.composequiz.database.dao.QuizDao
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.dto.QuizDTO
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

  fun findQuizByChapter(chapter: Int): Flow<List<Quiz>> {
    return quizDao.findByChapter(chapter)
  }

  fun findByFavoriteOrderByChapterIndex(): Flow<List<Quiz>> {
    return quizDao.findByFavoriteOrderByChapterIndex()
  }

  fun findByFavoriteOrderByFavoriteTime(): Flow<List<Quiz>> {
    return quizDao.findByFavoriteOrderByFavoriteTime()
  }

  fun findByFavoriteOrderByWrongAnswerCount(): Flow<List<Quiz>> {
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

  fun incWrongAnswerCount(id: Int) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.incWrongAnswerCount(id)
    }
  }
}