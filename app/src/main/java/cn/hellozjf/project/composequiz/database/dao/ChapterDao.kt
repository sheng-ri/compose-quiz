package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.Chapter

/**
 * 章节实体数据库操作
 */
@Dao
interface ChapterDao {

  @Insert
  fun insertChapter(chapter: Chapter)

  @Query("SELECT * FROM chapter")
  fun findAll(): List<Chapter>

  @Query("DELETE FROM chapter")
  fun deleteAll()
}