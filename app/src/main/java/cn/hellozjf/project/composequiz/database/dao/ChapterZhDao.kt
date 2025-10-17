package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.dto.ChapterDTO
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

  @Query("""
    SELECT
        chapter_zh.id id,
        chapter_zh.`index` `index`,
        chapter_zh.full_title fullTitle,
        chapter_zh.simple_title simpleTitle,
        chapter_zh.simple_url simpleUrl,
        chapter_zh.full_url fullUrl
    FROM chapter_zh
    ORDER BY chapter_zh.`index`
  """)
  fun findDTOFlowOrderByIndex(): Flow<List<ChapterDTO>>

  @Query("SELECT * FROM chapter_zh where `index` = :index")
  fun findByIndexFlow(index: Int): Flow<List<ChapterZh>>

  @Query("""
    SELECT
        chapter_zh.id id,
        chapter_zh.`index` `index`,
        chapter_zh.full_title fullTitle,
        chapter_zh.simple_title simpleTitle,
        chapter_zh.simple_url simpleUrl,
        chapter_zh.full_url fullUrl
    FROM chapter_zh
    WHERE `index` = :index
  """)
  fun findDTOFlowByIndex(index: Int): Flow<List<ChapterDTO>>

  @Query("SELECT * FROM chapter_zh where `index` = :index")
  suspend fun findByIndex(index: Int): ChapterZh?

  @Query("""
    SELECT
        chapter_zh.id id,
        chapter_zh.`index` `index`,
        chapter_zh.full_title fullTitle,
        chapter_zh.simple_title simpleTitle,
        chapter_zh.simple_url simpleUrl,
        chapter_zh.full_url fullUrl
    FROM chapter_zh
    WHERE `index` = :index
  """)
  suspend fun findDTOByIndex(index: Int): ChapterDTO?

  @Query("DELETE FROM chapter_zh")
  suspend fun deleteAll()

  @Query("SELECT count(*) FROM chapter_zh")
  fun getCount(): Int
}