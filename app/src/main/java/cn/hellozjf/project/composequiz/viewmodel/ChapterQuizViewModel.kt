package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.database.repository.QuizRepository
import cn.hellozjf.project.composequiz.dto.QuizDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChapterQuizViewModel(application: Application) : ViewModel() {

  private val repository: QuizRepository

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val quizEnDao = quizDb.quizEnDao()
    val quizZhDao = quizDb.quizZhDao()
    val quizExtDao = quizDb.quizExtDao()
    repository = QuizRepository(
      quizEnDao = quizEnDao,
      quizZhDao = quizZhDao,
      quizExtDao = quizExtDao
    )
  }

  fun insertQuiz(quizEn: QuizEn) {
    viewModelScope.launch {
      repository.insertQuiz(quizEn)
    }
  }

  fun insertQuiz(quizZh: QuizZh) {
    viewModelScope.launch {
      repository.insertQuiz(quizZh)
    }
  }

  fun findQuizDTOFlowByChapterIndex(
    language: String,
    chapterIndex: Int
  ): Flow<List<QuizDTO>> {
    return repository.findQuizDTOFlowByChapterIndex(
      language = language,
      chapterIndex = chapterIndex
    )
  }

  suspend fun findQuizDTOListByChapterIndex(
    language: String,
    chapterIndex: Int
  ): List<QuizDTO> {
    return repository.findQuizDTOListByChapterIndex(
      language = language,
      chapterIndex = chapterIndex
    )
  }

  suspend fun findQuizListByFavorite(
    language: String
  ): List<QuizDTO> {
    return repository.findQuizDTOListByFavorite(
      language = language
    )
  }

  suspend fun findByChapterIndexAndQuizIndex(
    language: String,
    chapterIndex: Int,
    quizIndex: Int
  ): QuizDTO? {
    return repository.findByChapterIndexAndQuizIndex(
      language = language,
      chapterIndex = chapterIndex,
      quizIndex = quizIndex
    )
  }

  fun findDTOFlowByFavoriteOrderByChapterIndex(
    language: String
  ): Flow<List<QuizDTO>> {
    return repository.findDTOFlowByFavoriteOrderByChapterIndex(
      language = language
    )
  }

  fun findDTOFlowByFavoriteOrderByFavoriteTime(
    language: String
  ): Flow<List<QuizDTO>> {
    return repository.findDTOFlowByFavoriteOrderByFavoriteTime(
      language = language
    )
  }

  fun findDTOFlowByFavoriteOrderByWrongAnswerCount(
    language: String
  ): Flow<List<QuizDTO>> {
    return repository.findDTOFlowByFavoriteOrderByWrongAnswerCount(
      language = language
    )
  }

  suspend fun findDTOListByFavoriteOrderByChapterIndex(
    language: String
  ): List<QuizDTO> {
    return repository.findDTOListByFavoriteOrderByChapterIndex(
      language = language
    )
  }

  suspend fun findDTOListByFavoriteOrderByFavoriteTime(
    language: String
  ): List<QuizDTO> {
    return repository.findDTOListByFavoriteOrderByFavoriteTime(
      language = language
    )
  }

  suspend fun findDTOListByFavoriteOrderByWrongAnswerCount(
    language: String
  ): List<QuizDTO> {
    return repository.findDTOListByFavoriteOrderByWrongAnswerCount(
      language = language
    )
  }

  fun deleteQuizByChapter(chapterIndex: Int) {
    viewModelScope.launch {
      repository.deleteQuizByChapterIndex(chapterIndex)
    }
  }

  suspend fun setFavorite(
    chapterIndex: Int,
    quizIndex: Int,
    favorite: Boolean,
    favoriteTime: Long
  ) {
    repository.setFavorite(
      chapterIndex = chapterIndex,
      quizIndex = quizIndex,
      favorite = favorite,
      favoriteTime = favoriteTime
    )
  }

  suspend fun incWrongAnswerCount(
    chapterIndex: Int,
    quizIndex: Int
  ) {
    repository.incWrongAnswerCount(
      chapterIndex = chapterIndex,
      quizIndex = quizIndex
    )
  }
}