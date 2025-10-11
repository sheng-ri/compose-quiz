package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.Chapter
import kotlinx.coroutines.flow.Flow

/**
 * 章节实体数据库操作
 */
@Dao
interface ChapterDao {

  @Insert
  fun insertChapter(chapter: Chapter)

  @Query("SELECT * FROM chapter order by `index`")
  fun findAllOrderByIndex(): Flow<List<Chapter>>

  @Query("DELETE FROM chapter")
  fun deleteAll()

  @Query("SELECT count(*) FROM chapter")
  fun getCount(): Int
}