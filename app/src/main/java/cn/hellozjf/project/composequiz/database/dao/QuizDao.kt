package cn.hellozjf.project.composequiz.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.Product
import cn.hellozjf.project.composequiz.database.entity.Quiz

/**
 * 问答实体数据库操作
 */
@Dao
interface QuizDao {

  @Insert
  fun insertQuiz(quiz: Quiz)

  @Query("SELECT * FROM quiz WHERE chapter_index = :chapterIndex")
  fun findByChapter(chapterIndex: Int): List<Quiz>

  @Query("DELETE FROM quiz WHERE chapter_index = :chapterIndex")
  fun deleteByChapter(chapterIndex: Int)

  @Query("SELECT count(*) FROM quiz")
  fun getCount(): Int
}