package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.dto.QuizDTO
import kotlinx.coroutines.flow.Flow

/**
 * 问答实体数据库操作
 */
@Dao
interface QuizZhDao {

  @Insert
  suspend fun insertQuiz(quiz: QuizZh)

  @Query("SELECT * FROM quiz_zh WHERE chapter_index = :chapterIndex")
  fun findFlowByChapterIndex(chapterIndex: Int): Flow<List<QuizZh>>

  @Query("""
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
    WHERE chapter_index = :chapterIndex
  """)
  fun findQuizDTOFlowByChapterIndex(chapterIndex: Int): Flow<List<QuizDTO>>

  @Query("SELECT * FROM quiz_zh WHERE chapter_index = :chapterIndex")
  suspend fun findByChapterIndex(chapterIndex: Int): List<QuizZh>

  @Query("""
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
    WHERE chapter_index = :chapterIndex
  """)
  suspend fun findQuizDTOByChapterIndex(chapterIndex: Int): List<QuizDTO>

//  @Query("SELECT * FROM quiz_zh WHERE favorite = 1")
//  suspend fun findByFavorite(): List<QuizZh>
//
//  @Query("SELECT * FROM quiz_zh WHERE id IN (:idList)")
//  fun findByIdListFlow(idList: List<Int>): Flow<List<QuizZh>>
//
//  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY chapter_index")
//  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<QuizZh>>
//
//  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY chapter_index")
//  suspend fun findByFavoriteOrderByChapterIndex(): List<QuizZh>
//
//  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY favorite_time")
//  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<QuizZh>>
//
//  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY favorite_time")
//  suspend fun findByFavoriteOrderByFavoriteTime(): List<QuizZh>
//
//  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY wrong_answer_count")
//  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<QuizZh>>
//
//  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY wrong_answer_count")
//  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<QuizZh>

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

  @Query("""
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
    WHERE chapter_index=:chapterIndex and quiz_index=:quizIndex
  """)
  suspend fun findQuizDTOByChapterIndexAndQuizIndex(
    chapterIndex: Int,
    quizIndex: Int
  ): QuizDTO?
}