package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.repository.QuizEnRepository
import cn.hellozjf.project.composequiz.dto.QuizDTO
import kotlinx.coroutines.flow.Flow

class ChapterQuizEnViewModel(application: Application) : ViewModel() {
  private val repository: QuizEnRepository

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val quizDao = quizDb.quizEnDao()
    repository = QuizEnRepository(quizDao)
  }

  fun insertQuiz(quizEn: QuizEn) {
    repository.insertQuiz(quizEn)
  }

  fun findQuizFlowByChapter(chapter: Int): Flow<List<QuizEn>> {
    return repository.findQuizFlowByChapter(chapter)
  }

  suspend fun findQuizByChapterIndex(chapter: Int): List<QuizEn> {
    return repository.findQuizByChapter(chapter)
  }

  suspend fun findQuizByFavorite(): List<QuizEn> {
    return repository.findQuizByFavorite()
  }

  fun findByIdList(idList: List<Int>): Flow<List<QuizEn>> {
    return repository.findByIdListFlow(idList)
  }

  suspend fun findByChapterIndexAndQuizIndex(
    chapterIndex: Int,
    quizIndex: Int
  ) : QuizEn? {
    return repository.findByChapterIndexAndQuizIndex(
      chapterIndex = chapterIndex,
      quizIndex = quizIndex
    )
  }

  suspend fun findQuizDTOByChapterIndexAndQuizIndex(
    chapterIndex: Int,
    quizIndex: Int
  ) : QuizDTO? {
    val quiz = findByChapterIndexAndQuizIndex(
      chapterIndex = chapterIndex,
      quizIndex = quizIndex
    )
    return quiz?.let {
      QuizDTO(
        id = it.id,
        chapterIndex = it.chapterIndex,
        quizIndex = it.quizIndex,
        question = it.question,
        correctOption = it.correctOption,
        wrongOption1 = it.wrongOption1,
        wrongOption2 = it.wrongOption2,
        wrongOption3 = it.wrongOption3,
        explanation = it.explanation
      )
    }
  }

  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<QuizEn>> {
    return repository.findByFavoriteOrderByChapterIndexFlow()
  }

  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<QuizEn>> {
    return repository.findByFavoriteOrderByFavoriteTimeFlow()
  }

  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<QuizEn>> {
    return repository.findByFavoriteOrderByWrongAnswerCountFlow()
  }

  suspend fun findByFavoriteOrderByChapterIndex(): List<QuizEn> {
    return repository.findByFavoriteOrderByChapterIndex()
  }

  suspend fun findByFavoriteOrderByFavoriteTime(): List<QuizEn> {
    return repository.findByFavoriteOrderByFavoriteTime()
  }

  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<QuizEn> {
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