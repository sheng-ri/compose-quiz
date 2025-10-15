package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.hellozjf.project.composequiz.database.entity.QuizExt

/**
 * 问答实体扩展数据库操作
 */
@Dao
interface QuizExtDao {

  @Insert
  suspend fun insert(quizExt: QuizExt)

  @Delete
  suspend fun delete(quizExt: QuizExt)

  @Update
  suspend fun update(quizExt: QuizExt)

  @Query("select * from quiz_ext where chapter_index=:chapterIndex and quiz_index=:quizIndex")
  suspend fun query(
    chapterIndex: Int,
    quizIndex: Int
  ) : QuizExt?

  @Query("SELECT * FROM quiz_ext WHERE favorite = 1")
  suspend fun findByFavorite(): List<QuizExt>
}