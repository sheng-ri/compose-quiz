package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizExt
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * 问答实体数据库操作
 */
@Dao
interface QuizEnDao {

  @Insert
  suspend fun insertQuiz(quizEn: QuizEn)

  @Query("""
    SELECT * 
    FROM quiz_en 
    WHERE chapter_index = :chapterIndex
    ORDER BY create_time
  """)
  fun findFlowByChapterIndex(chapterIndex: Int): Flow<List<QuizEn>>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext ON quiz_en.chapter_index = quiz_ext.chapter_index AND quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_en.chapter_index = :chapterIndex
    ORDER BY quiz_en.create_time
  """
  )
  fun findQuizDTOFlowByChapterIndex(chapterIndex: Int): Flow<List<QuizDTO>>

  @Query("""
    SELECT * 
    FROM quiz_en 
    WHERE chapter_index = :chapterIndex
    ORDER BY create_time
  """)
  suspend fun findByChapterIndex(chapterIndex: Int): List<QuizEn>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext ON quiz_en.chapter_index = quiz_ext.chapter_index AND quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_en.chapter_index = :chapterIndex
    ORDER BY quiz_en.create_time
  """
  )
  suspend fun findQuizDTOByChapterIndex(chapterIndex: Int): List<QuizDTO>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en 
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1
    ORDER BY quiz_en.create_time
  """
  )
  suspend fun findByFavorite(): List<QuizDTO>

//  @Query("SELECT * FROM quiz_en WHERE id IN (:idList)")
//  fun findByIdListFlow(idList: List<Int>): Flow<List<QuizEn>>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.chapter_index, quiz_ext.quiz_index
  """
  )
  fun findDTOFlowByFavoriteOrderByChapterIndex(): Flow<List<QuizDTO>>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.chapter_index, quiz_ext.quiz_index
  """
  )
  suspend fun findDTOListByFavoriteOrderByChapterIndex(): List<QuizDTO>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.favorite_time
  """
  )
  fun findDTOFlowByFavoriteOrderByFavoriteTime(): Flow<List<QuizDTO>>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.favorite_time
  """
  )
  suspend fun findDTOListByFavoriteOrderByFavoriteTime(): List<QuizDTO>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.wrong_answer_count
  """
  )
  fun findDTOFlowByFavoriteOrderByWrongAnswerCount(): Flow<List<QuizDTO>>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.wrong_answer_count
  """
  )
  fun findDTOListByFavoriteOrderByWrongAnswerCount(): List<QuizDTO>

  @Query("DELETE FROM quiz_en WHERE chapter_index = :chapterIndex")
  suspend fun deleteByChapter(chapterIndex: Int)

  @Query("SELECT count(*) FROM quiz_en")
  suspend fun getCount(): Int

//  @Query("UPDATE quiz_en SET favorite = :favorite, favorite_time = :favoriteTime WHERE id = :id")
//  suspend fun setFavorite(id: Int, favorite: Boolean, favoriteTime: Long)
//
//  @Query("UPDATE quiz_en SET wrong_answer_count = wrong_answer_count + 1 WHERE id = :id")
//  suspend fun incWrongAnswerCount(id: Int)

  @Query("""
    SELECT * 
    FROM quiz_en 
    WHERE chapter_index=:chapterIndex and quiz_index=:quizIndex
  """)
  suspend fun findByChapterIndexAndQuizIndex(chapterIndex: Int, quizIndex: Int): QuizEn?

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    WHERE quiz_en.chapter_index=:chapterIndex and quiz_en.quiz_index=:quizIndex
  """
  )
  suspend fun findQuizDTOByKey(
    chapterIndex: Int,
    quizIndex: Int
  ): QuizDTO?


//  fun findFlowByKeyList(quizKeyList: List<QuizKey>): Flow<List<QuizDTO>> {
//    if (quizKeyList.isEmpty()) {
//      return flowOf(emptyList())
//    }
//    val placeholders = quizKeyList.joinToString(",") {
//      "(${it.chapterIndex},${it.quizIndex})"
//    }
//    return findFlowByKeyListRaw(placeholders)
//  }
//
//  @Query(
//    """
//    SELECT
//        quiz_en.chapter_index chapterIndex,
//        quiz_en.quiz_index quizIndex,
//        quiz_en.question question,
//        quiz_en.correct_option correctOption,
//        quiz_en.wrong_option1 wrongOption1,
//        quiz_en.wrong_option2 wrongOption2,
//        quiz_en.wrong_option3 wrongOption3,
//        quiz_en.explanation explanation,
//        quiz_ext.favorite favorite,
//        quiz_ext.favorite_time favoriteTime,
//        quiz_ext.wrong_answer_count wrongAnswerCount
//    FROM quiz_en
//    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
//    WHERE (quiz_en.chapter_index, quiz_en.quiz_index) IN (:placeholders)
//  """
//  )
//  fun findFlowByKeyListRaw(placeholders: String): Flow<List<QuizDTO>>

  fun findFlowByKeyList(quizKeyList: List<QuizKey>): Flow<List<QuizDTO>> {
    if (quizKeyList.isEmpty()) {
      return flowOf(emptyList())
    }

    val placeholders = quizKeyList.joinToString(",") {
      "(${it.chapterIndex},${it.quizIndex})"
    }

    val query = SimpleSQLiteQuery(
      """
            SELECT 
                quiz_en.chapter_index chapterIndex,
                quiz_en.quiz_index quizIndex,
                quiz_en.question question,
                quiz_en.correct_option correctOption,
                quiz_en.wrong_option1 wrongOption1,
                quiz_en.wrong_option2 wrongOption2,
                quiz_en.wrong_option3 wrongOption3,
                quiz_en.explanation explanation,
                quiz_ext.favorite favorite,
                quiz_ext.favorite_time favoriteTime,
                quiz_ext.wrong_answer_count wrongAnswerCount
            FROM quiz_en
            LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index 
                              and quiz_en.quiz_index = quiz_ext.quiz_index
            WHERE (quiz_en.chapter_index, quiz_en.quiz_index) IN ($placeholders)
            ORDER BY quiz_en.create_time
            """
    )

    return findFlowByRawQuery(query)
  }

  @RawQuery(observedEntities = [QuizEn::class, QuizExt::class])
  fun findFlowByRawQuery(query: SupportSQLiteQuery): Flow<List<QuizDTO>>

  @Query(
    """
    SELECT 
        quiz_en.chapter_index chapterIndex,
        quiz_en.quiz_index quizIndex,
        quiz_en.question question,
        quiz_en.correct_option correctOption,
        quiz_en.wrong_option1 wrongOption1,
        quiz_en.wrong_option2 wrongOption2,
        quiz_en.wrong_option3 wrongOption3,
        quiz_en.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_en
    LEFT JOIN quiz_ext on quiz_en.chapter_index = quiz_ext.chapter_index and quiz_en.quiz_index = quiz_ext.quiz_index
    ORDER BY quiz_en.create_time
  """
  )
  suspend fun findQuizDTOList(): List<QuizDTO>
}