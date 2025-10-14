package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.Quiz
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import kotlinx.coroutines.flow.Flow

/**
 * 问答实体数据库操作
 */
@Dao
interface QuizZhDao {

  @Insert
  suspend fun insertQuiz(quiz: QuizZh)

  @Query("SELECT * FROM quiz_zh WHERE chapter_index = :chapterIndex")
  fun findFlowByChapter(chapterIndex: Int): Flow<List<QuizZh>>

  @Query("SELECT * FROM quiz_zh WHERE chapter_index = :chapterIndex")
  suspend fun findByChapter(chapterIndex: Int): List<QuizZh>

  @Query("SELECT * FROM quiz_zh WHERE favorite = 1")
  suspend fun findByFavorite(): List<QuizZh>

  @Query("SELECT * FROM quiz_zh WHERE id IN (:idList)")
  fun findByIdListFlow(idList: List<Int>): Flow<List<QuizZh>>

  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY chapter_index")
  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<QuizZh>>

  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY chapter_index")
  suspend fun findByFavoriteOrderByChapterIndex(): List<QuizZh>

  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY favorite_time")
  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<QuizZh>>

  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY favorite_time")
  suspend fun findByFavoriteOrderByFavoriteTime(): List<QuizZh>

  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY wrong_answer_count")
  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<QuizZh>>

  @Query("SELECT * FROM quiz_zh WHERE favorite = 1 ORDER BY wrong_answer_count")
  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<QuizZh>

  @Query("DELETE FROM quiz_zh WHERE chapter_index = :chapterIndex")
  suspend fun deleteByChapter(chapterIndex: Int)

  @Query("SELECT count(*) FROM quiz_zh")
  fun getCount(): Int

  @Query("UPDATE quiz_zh SET favorite = :favorite, favorite_time = :favoriteTime WHERE id = :id")
  suspend fun setFavorite(id: Int, favorite: Boolean, favoriteTime: Long)

  @Query("UPDATE quiz_zh SET wrong_answer_count = wrong_answer_count + 1 WHERE id = :id")
  suspend fun incWrongAnswerCount(id: Int)
}