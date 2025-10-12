package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.Quiz
import kotlinx.coroutines.flow.Flow

/**
 * 问答实体数据库操作
 */
@Dao
interface QuizDao {

  @Insert
  fun insertQuiz(quiz: Quiz)

  @Query("SELECT * FROM quiz WHERE chapter_index = :chapterIndex")
  fun findFlowByChapter(chapterIndex: Int): Flow<List<Quiz>>

  @Query("SELECT * FROM quiz WHERE chapter_index = :chapterIndex")
  suspend fun findByChapter(chapterIndex: Int): List<Quiz>

  @Query("SELECT * FROM quiz WHERE favorite = 1")
  suspend fun findByFavorite(): List<Quiz>

  @Query("SELECT * FROM quiz WHERE id IN (:idList)")
  fun findByIdListFlow(idList: List<Int>): Flow<List<Quiz>>

  @Query("SELECT * FROM quiz WHERE favorite = 1 ORDER BY chapter_index")
  fun findByFavoriteOrderByChapterIndexFlow(): Flow<List<Quiz>>

  @Query("SELECT * FROM quiz WHERE favorite = 1 ORDER BY chapter_index")
  suspend fun findByFavoriteOrderByChapterIndex(): List<Quiz>

  @Query("SELECT * FROM quiz WHERE favorite = 1 ORDER BY favorite_time")
  fun findByFavoriteOrderByFavoriteTimeFlow(): Flow<List<Quiz>>

  @Query("SELECT * FROM quiz WHERE favorite = 1 ORDER BY favorite_time")
  suspend fun findByFavoriteOrderByFavoriteTime(): List<Quiz>

  @Query("SELECT * FROM quiz WHERE favorite = 1 ORDER BY wrong_answer_count")
  fun findByFavoriteOrderByWrongAnswerCountFlow(): Flow<List<Quiz>>

  @Query("SELECT * FROM quiz WHERE favorite = 1 ORDER BY wrong_answer_count")
  suspend fun findByFavoriteOrderByWrongAnswerCount(): List<Quiz>

  @Query("DELETE FROM quiz WHERE chapter_index = :chapterIndex")
  fun deleteByChapter(chapterIndex: Int)

  @Query("SELECT count(*) FROM quiz")
  fun getCount(): Int

  @Query("UPDATE quiz SET favorite = :favorite, favorite_time = :favoriteTime WHERE id = :id")
  fun setFavorite(id: Int, favorite: Boolean, favoriteTime: Long)

  @Query("UPDATE quiz SET wrong_answer_count = wrong_answer_count + 1 WHERE id = :id")
  fun incWrongAnswerCount(id: Int)
}