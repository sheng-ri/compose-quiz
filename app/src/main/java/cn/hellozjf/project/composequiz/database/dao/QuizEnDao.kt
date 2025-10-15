package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.dto.QuizDTO
import kotlinx.coroutines.flow.Flow

/**
 * 问答实体数据库操作
 */
@Dao
interface QuizEnDao {

  @Insert
  suspend fun insertQuiz(quizEn: QuizEn)

  @Query("SELECT * FROM quiz_en WHERE chapter_index = :chapterIndex")
  fun findFlowByChapterIndex(chapterIndex: Int): Flow<List<QuizEn>>

  @Query("""
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
    WHERE chapter_index = :chapterIndex
  """)
  fun findQuizDTOFlowByChapterIndex(chapterIndex: Int): Flow<List<QuizDTO>>

  @Query("SELECT * FROM quiz_en WHERE chapter_index = :chapterIndex")
  suspend fun findByChapterIndex(chapterIndex: Int): List<QuizEn>

  @Query("""
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
    WHERE chapter_index = :chapterIndex
  """)
  suspend fun findQuizDTOByChapterIndex(chapterIndex: Int): Flow<List<QuizDTO>>

//  @Query("SELECT * FROM quiz_en WHERE favorite = 1")
//  suspend fun findByFavorite(): List<QuizEn>
//
//  @Query("SELECT * FROM quiz_en WHERE id IN (:idList)")
//  fun findByIdListFlow(idList: List<Int>): Flow<List<QuizEn>>

//  @Query("SELECT * FROM quiz_en WHERE favorite = 1 ORDER BY chapter_index")
//  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<QuizEn>>
//
//  @Query("SELECT * FROM quiz_en WHERE favorite = 1 ORDER BY chapter_index")
//  suspend fun findByFavoriteOrderByChapterIndex(): List<QuizEn>
//
//  @Query("SELECT * FROM quiz_en WHERE favorite = 1 ORDER BY favorite_time")
//  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<QuizEn>>
//
//  @Query("SELECT * FROM quiz_en WHERE favorite = 1 ORDER BY favorite_time")
//  suspend fun findByFavoriteOrderByFavoriteTime(): List<QuizEn>
//
//  @Query("SELECT * FROM quiz_en WHERE favorite = 1 ORDER BY wrong_answer_count")
//  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<QuizEn>>
//
//  @Query("SELECT * FROM quiz_en WHERE favorite = 1 ORDER BY wrong_answer_count")
//  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<QuizEn>

  @Query("DELETE FROM quiz_en WHERE chapter_index = :chapterIndex")
  suspend fun deleteByChapter(chapterIndex: Int)

  @Query("SELECT count(*) FROM quiz_en")
  suspend fun getCount(): Int

//  @Query("UPDATE quiz_en SET favorite = :favorite, favorite_time = :favoriteTime WHERE id = :id")
//  suspend fun setFavorite(id: Int, favorite: Boolean, favoriteTime: Long)
//
//  @Query("UPDATE quiz_en SET wrong_answer_count = wrong_answer_count + 1 WHERE id = :id")
//  suspend fun incWrongAnswerCount(id: Int)

  @Query("SELECT * FROM quiz_en WHERE chapter_index=:chapterIndex and quiz_index=:quizIndex")
  suspend fun findByChapterIndexAndQuizIndex(chapterIndex: Int, quizIndex: Int): QuizEn?

  @Query("""
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
    WHERE chapter_index=:chapterIndex and quiz_index=:quizIndex
  """)
  suspend fun findQuizDTOByChapterIndexAndQuizIndex(
    chapterIndex: Int,
    quizIndex: Int
  ): QuizDTO?
}