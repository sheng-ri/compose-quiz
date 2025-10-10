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
  fun findByChapter(chapterIndex: Int): Flow<List<Quiz>>

  @Query("DELETE FROM quiz WHERE chapter_index = :chapterIndex")
  fun deleteByChapter(chapterIndex: Int)

  @Query("SELECT count(*) FROM quiz")
  fun getCount(): Int

  @Query("UPDATE quiz SET favorite = :favorite WHERE id = :id")
  fun setFavorite(id: Int, favorite: Boolean)
}