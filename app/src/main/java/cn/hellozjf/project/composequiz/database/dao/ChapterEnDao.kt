package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import kotlinx.coroutines.flow.Flow

/**
 * 章节实体数据库操作
 */
@Dao
interface ChapterEnDao {

  @Insert
  suspend fun insertChapter(chapterEn: ChapterEn)

  @Query("SELECT * FROM chapter_en order by `index`")
  fun findAllOrderByIndex(): Flow<List<ChapterEn>>

  @Query("""
    SELECT
        chapter_en.`index` `index`,
        chapter_en.full_title fullTitle,
        chapter_en.simple_title simpleTitle,
        chapter_en.simple_url simpleUrl,
        chapter_en.full_url fullUrl
    FROM chapter_en
    ORDER BY chapter_en.`index`
  """)
  fun findDTOFlowOrderByIndex(): Flow<List<ChapterDTO>>

  @Query("""
    SELECT * 
    FROM chapter_en 
    WHERE `index` = :index
    ORDER BY `index`
  """)
  fun findByIndexFlow(index: Int): Flow<List<ChapterEn>>

  @Query("""
    SELECT
        chapter_en.`index` `index`,
        chapter_en.full_title fullTitle,
        chapter_en.simple_title simpleTitle,
        chapter_en.simple_url simpleUrl,
        chapter_en.full_url fullUrl
    FROM chapter_en
    WHERE `index` = :index
    ORDER BY `index`
  """)
  fun findChapterDTOFlowByIndex(index: Int): Flow<ChapterDTO?>

  @Query("""
    SELECT * 
    FROM chapter_en 
    WHERE `index` = :index
    ORDER BY `index`
  """)
  suspend fun findByIndex(index: Int): ChapterEn?

  @Query("""
    SELECT
        chapter_en.`index` `index`,
        chapter_en.full_title fullTitle,
        chapter_en.simple_title simpleTitle,
        chapter_en.simple_url simpleUrl,
        chapter_en.full_url fullUrl
    FROM chapter_en
    WHERE `index` = :index
    ORDER BY `index`
  """)
  suspend fun findChapterDTOByIndex(index: Int): ChapterDTO?

  @Query("DELETE FROM chapter_en")
  suspend fun deleteAll()

  @Query("SELECT count(*) FROM chapter_en")
  suspend fun getCount(): Int
}