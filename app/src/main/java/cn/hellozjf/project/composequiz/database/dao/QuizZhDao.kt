package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import cn.hellozjf.project.composequiz.database.entity.QuizExt
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.dto.QuizKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * 问答实体数据库操作
 */
@Dao
interface QuizZhDao {

  @Insert
  suspend fun insertQuiz(quiz: QuizZh)

  @Query("""
    SELECT * 
    FROM quiz_zh 
    WHERE chapter_index = :chapterIndex
    ORDER BY quiz_zh.chapter_index, quiz_zh.quiz_index
  """)
  fun findFlowByChapterIndex(chapterIndex: Int): Flow<List<QuizZh>>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh
    LEFT JOIN quiz_ext ON quiz_zh.chapter_index = quiz_ext.chapter_index AND quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_zh.chapter_index = :chapterIndex
    ORDER BY quiz_zh.chapter_index, quiz_zh.quiz_index
  """
  )
  fun findQuizDTOFlowByChapterIndex(chapterIndex: Int): Flow<List<QuizDTO>>

  @Query("""
    SELECT * 
    FROM quiz_zh 
    WHERE chapter_index = :chapterIndex
    ORDER BY quiz_zh.chapter_index, quiz_zh.quiz_index
  """)
  suspend fun findByChapterIndex(chapterIndex: Int): List<QuizZh>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh
    LEFT JOIN quiz_ext ON quiz_zh.chapter_index = quiz_ext.chapter_index AND quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_zh.chapter_index = :chapterIndex
    ORDER BY quiz_zh.chapter_index, quiz_zh.quiz_index
  """
  )
  suspend fun findQuizDTOByChapterIndex(chapterIndex: Int): List<QuizDTO>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh 
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1
    ORDER BY quiz_zh.chapter_index, quiz_zh.quiz_index
  """
  )
  suspend fun findByFavorite(): List<QuizDTO>

//  @Query("SELECT * FROM quiz_zh WHERE id IN (:idList)")
//  fun findByIdListFlow(idList: List<Int>): Flow<List<QuizZh>>
//

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh 
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.chapter_index, quiz_ext.quiz_index
  """
  )
  fun findDTOFlowByFavoriteOrderByChapterIndex(): Flow<List<QuizDTO>>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh 
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.chapter_index, quiz_ext.quiz_index
  """
  )
  suspend fun findDTOListByFavoriteOrderByChapterIndex(): List<QuizDTO>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.favorite_time
  """
  )
  fun findDTOFlowByFavoriteOrderByFavoriteTime(): Flow<List<QuizDTO>>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.favorite_time
  """
  )
  suspend fun findDTOListByFavoriteOrderByFavoriteTime(): List<QuizDTO>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.wrong_answer_count
  """
  )
  fun findDTOFlowByFavoriteOrderByWrongAnswerCount(): Flow<List<QuizDTO>>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_ext.favorite = 1 
    ORDER BY quiz_ext.wrong_answer_count
  """
  )
  suspend fun findDTOListByFavoriteOrderByWrongAnswerCount(): List<QuizDTO>

  @Query("DELETE FROM quiz_zh WHERE chapter_index = :chapterIndex")
  suspend fun deleteByChapter(chapterIndex: Int)

  @Query("SELECT count(*) FROM quiz_zh")
  fun getCount(): Int

//  @Query("UPDATE quiz_zh SET favorite = :favorite, favorite_time = :favoriteTime WHERE id = :id")
//  suspend fun setFavorite(id: Int, favorite: Boolean, favoriteTime: Long)
//
//  @Query("UPDATE quiz_zh SET wrong_answer_count = wrong_answer_count + 1 WHERE id = :id")
//  suspend fun incWrongAnswerCount(id: Int)

  @Query("SELECT * FROM quiz_zh WHERE chapter_index=:chapterIndex and quiz_index=:quizIndex")
  fun findByChapterIndexAndQuizIndex(chapterIndex: Int, quizIndex: Int): QuizZh?

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    WHERE quiz_zh.chapter_index=:chapterIndex and quiz_zh.quiz_index=:quizIndex
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
//  @Query("""
//    SELECT
//        quiz_zh.chapter_index chapterIndex,
//        quiz_zh.quiz_index quizIndex,
//        quiz_zh.question question,
//        quiz_zh.correct_option correctOption,
//        quiz_zh.wrong_option1 wrongOption1,
//        quiz_zh.wrong_option2 wrongOption2,
//        quiz_zh.wrong_option3 wrongOption3,
//        quiz_zh.explanation explanation,
//        quiz_ext.favorite favorite,
//        quiz_ext.favorite_time favoriteTime,
//        quiz_ext.wrong_answer_count wrongAnswerCount
//    FROM quiz_zh
//    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
//    WHERE (quiz_zh.chapter_index, quiz_zh.quiz_index) IN (:placeholders)
//  """)
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
                quiz_zh.chapter_index chapterIndex,
                quiz_zh.quiz_index quizIndex,
                quiz_zh.question question,
                quiz_zh.correct_option correctOption,
                quiz_zh.wrong_option1 wrongOption1,
                quiz_zh.wrong_option2 wrongOption2,
                quiz_zh.wrong_option3 wrongOption3,
                quiz_zh.explanation explanation,
                quiz_ext.favorite favorite,
                quiz_ext.favorite_time favoriteTime,
                quiz_ext.wrong_answer_count wrongAnswerCount
            FROM quiz_zh
            LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index 
                              and quiz_zh.quiz_index = quiz_ext.quiz_index
            WHERE (quiz_zh.chapter_index, quiz_zh.quiz_index) IN ($placeholders)
            ORDER BY quiz_zh.chapter_index, quiz_zh.quiz_index
            """
    )

    return findFlowByRawQuery(query)
  }

  @RawQuery(observedEntities = [QuizZh::class, QuizExt::class])
  fun findFlowByRawQuery(query: SupportSQLiteQuery): Flow<List<QuizDTO>>

  @Query(
    """
    SELECT 
        quiz_zh.chapter_index chapterIndex,
        quiz_zh.quiz_index quizIndex,
        quiz_zh.question question,
        quiz_zh.correct_option correctOption,
        quiz_zh.wrong_option1 wrongOption1,
        quiz_zh.wrong_option2 wrongOption2,
        quiz_zh.wrong_option3 wrongOption3,
        quiz_zh.explanation explanation,
        quiz_ext.favorite favorite,
        quiz_ext.favorite_time favoriteTime,
        quiz_ext.wrong_answer_count wrongAnswerCount
    FROM quiz_zh
    LEFT JOIN quiz_ext on quiz_zh.chapter_index = quiz_ext.chapter_index and quiz_zh.quiz_index = quiz_ext.quiz_index
    ORDER BY quiz_zh.chapter_index, quiz_zh.quiz_index
  """
  )
  suspend fun findQuizDTOList(): List<QuizDTO>
}