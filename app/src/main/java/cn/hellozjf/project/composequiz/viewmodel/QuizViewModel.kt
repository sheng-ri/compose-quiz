package cn.hellozjf.project.composequiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizExt
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.database.repository.QuizRepository
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
  private val repository: QuizRepository
) : ViewModel() {

  suspend fun insertQuiz(quizEn: QuizEn) {
    repository.insertQuiz(quizEn)
  }

  suspend fun insertQuizExt(quizExt: QuizExt) {
    repository.insertQuizExt(quizExt)
  }

  suspend fun insertQuiz(quizZh: QuizZh) {
    repository.insertQuiz(quizZh)
  }

  suspend fun getCount(
    language: String
  ): Int {
    return repository.getCount(language)
  }

  suspend fun getExtCount(): Int {
    return repository.getExtCount()
  }

  fun findQuizDTOListFlowByChapterIndex(
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

  suspend fun findQuizKeyList(
    language: String
  ): List<QuizKey> {
    return repository.findQuizDTOList(language).map {
      QuizKey(
        chapterIndex = it.chapterIndex,
        quizIndex = it.quizIndex
      )
    }
  }

  suspend fun findQuizDTOByKey(
    language: String,
    quizKey: QuizKey
  ): QuizDTO? {
    return repository.findQuizDTOByKey(
      language = language,
      chapterIndex = quizKey.chapterIndex,
      quizIndex = quizKey.quizIndex
    )
  }

  fun findFlowByKeyList(
    language: String,
    quizKeyList: List<QuizKey>
  ): Flow<List<QuizDTO>> {
    return repository.findFlowByKeyList(
      language = language,
      quizKeyList = quizKeyList
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