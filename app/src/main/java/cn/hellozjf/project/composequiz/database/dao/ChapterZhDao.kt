package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import kotlinx.coroutines.flow.Flow

/**
 * 章节实体数据库操作
 */
@Dao
interface ChapterZhDao {

  @Insert
  suspend fun insertChapter(chapter: ChapterZh)

  @Query("SELECT * FROM chapter_zh order by `index`")
  fun findAllOrderByIndex(): Flow<List<ChapterZh>>

  @Query("SELECT * FROM chapter_zh where `index` = :index")
  fun findByIndexFlow(index: Int): Flow<List<ChapterZh>>

  @Query("SELECT * FROM chapter_zh where `index` = :index")
  suspend fun findByIndex(index: Int): ChapterZh?

  @Query("DELETE FROM chapter_zh")
  suspend fun deleteAll()

  @Query("SELECT count(*) FROM chapter_zh")
  fun getCount(): Int
}