package cn.hellozjf.project.composequiz.database.repository

import cn.hellozjf.project.composequiz.database.dao.QuizEnDao
import cn.hellozjf.project.composequiz.database.dao.QuizExtDao
import cn.hellozjf.project.composequiz.database.dao.QuizZhDao
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.util.LanguageConstant
import kotlinx.coroutines.flow.Flow

class QuizRepository(
  private val quizEnDao: QuizEnDao,
  private val quizZhDao: QuizZhDao,
  private val quizExtDao: QuizExtDao
) {

  /**
   * 添加英文版本的 Quiz
   */
  suspend fun insertQuiz(quizEn: QuizEn) {
    quizEnDao.insertQuiz(quizEn)
  }

  /**
   * 添加中文版本的 Quiz
   */
  suspend fun insertQuiz(quizZh: QuizZh) {
    quizZhDao.insertQuiz(quizZh)
  }

  /**
   * 根据章节编号删除 Quiz
   */
  suspend fun deleteQuizByChapterIndex(chapterIndex: Int) {
    quizEnDao.deleteByChapter(chapterIndex)
    quizZhDao.deleteByChapter(chapterIndex)
  }

  /**
   * 根据章节编号查找 Quiz 流
   */
  fun findQuizDTOFlowByChapterIndex(
    language: String,
    chapterIndex: Int
  ): Flow<List<QuizDTO>> {
    return if (language == LanguageConstant.ZH) {
      // 中文
      quizZhDao.findQuizDTOFlowByChapterIndex(chapterIndex)
    } else {
      // 英文
      quizEnDao.findQuizDTOFlowByChapterIndex(chapterIndex)
    }
  }

  /**
   * 根据章节编号查找 Quiz 列表
   */
  suspend fun findQuizDTOListByChapterIndex(
    language: String,
    chapterIndex: Int,
  ): List<QuizDTO> {
    return if (language == LanguageConstant.ZH) {
      // 中文
      quizZhDao.findQuizDTOByChapterIndex(chapterIndex)
    } else {
      quizEnDao.findQuizDTOByChapterIndex(chapterIndex)
    }
  }

  suspend fun findQuizDTOListByFavorite(
    language: String
  ): List<QuizDTO> {
    return if (language == LanguageConstant.ZH) {
      quizZhDao.findByFavorite()
    } else {
      quizEnDao.findByFavorite()
    }
  }

  fun findDTOFlowByFavoriteOrderByChapterIndex(
    language: String
  ): Flow<List<QuizDTO>> {
    return if (language == LanguageConstant.ZH) {
      quizZhDao.findDTOFlowByFavoriteOrderByChapterIndex()
    } else {
      quizEnDao.findDTOFlowByFavoriteOrderByChapterIndex()
    }
  }

  suspend fun findDTOListByFavoriteOrderByChapterIndex(
    language: String
  ): List<QuizDTO> {
    return if (language == LanguageConstant.ZH) {
      quizZhDao.findDTOListByFavoriteOrderByChapterIndex()
    } else {
      quizEnDao.findDTOListByFavoriteOrderByChapterIndex()
    }
  }

  fun findDTOFlowByFavoriteOrderByFavoriteTime(
    language: String
  ): Flow<List<QuizDTO>> {
    return if (language == LanguageConstant.ZH) {
      quizZhDao.findDTOFlowByFavoriteOrderByFavoriteTime()
    } else {
      quizEnDao.findDTOFlowByFavoriteOrderByFavoriteTime()
    }
  }

  suspend fun findDTOListByFavoriteOrderByFavoriteTime(
    language: String
  ): List<QuizDTO> {
    return if (language == LanguageConstant.ZH) {
      quizZhDao.findDTOListByFavoriteOrderByFavoriteTime()
    } else {
      quizEnDao.findDTOListByFavoriteOrderByFavoriteTime()
    }
  }

  fun findDTOFlowByFavoriteOrderByWrongAnswerCount(
    language: String
  ): Flow<List<QuizDTO>> {
    return if (language == LanguageConstant.ZH) {
      quizZhDao.findDTOFlowByFavoriteOrderByWrongAnswerCount()
    } else {
      quizEnDao.findDTOFlowByFavoriteOrderByWrongAnswerCount()
    }
  }

  suspend fun findDTOListByFavoriteOrderByWrongAnswerCount(
    language: String
  ): List<QuizDTO> {
    return if (language == LanguageConstant.ZH) {
      quizZhDao.findDTOListByFavoriteOrderByWrongAnswerCount()
    } else {
      quizEnDao.findDTOListByFavoriteOrderByWrongAnswerCount()
    }
  }

  suspend fun setFavorite(
    chapterIndex: Int,
    quizIndex: Int,
    favorite: Boolean,
    favoriteTime: Long
  ) {
    val quizExt = quizExtDao.query(chapterIndex, quizIndex)
    quizExt?.let {
      quizExtDao.update(
        it.copy(
          favorite = favorite,
          favoriteTime = favoriteTime
        )
      )
    }
  }

  suspend fun incWrongAnswerCount(
    chapterIndex: Int,
    quizIndex: Int
  ) {
    val quizExt = quizExtDao.query(chapterIndex, quizIndex)
    quizExt?.let {
      quizExtDao.update(
        it.copy(
          wrongAnswerCount = it.wrongAnswerCount + 1
        )
      )
    }
  }

  suspend fun findByChapterIndexAndQuizIndex(
    language: String,
    chapterIndex: Int,
    quizIndex: Int
  ): QuizDTO? {
    return if (language == LanguageConstant.ZH) {
      quizZhDao.findQuizDTOByChapterIndexAndQuizIndex(
        chapterIndex = chapterIndex,
        quizIndex = quizIndex
      )
    } else {
      quizEnDao.findQuizDTOByChapterIndexAndQuizIndex(
        chapterIndex = chapterIndex,
        quizIndex = quizIndex
      )
    }
  }
}