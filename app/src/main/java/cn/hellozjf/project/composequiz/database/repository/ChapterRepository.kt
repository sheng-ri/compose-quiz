package cn.hellozjf.project.composequiz.database.repository

import cn.hellozjf.project.composequiz.database.dao.ChapterEnDao
import cn.hellozjf.project.composequiz.database.dao.ChapterZhDao
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.util.LanguageConstant
import kotlinx.coroutines.flow.Flow

class ChapterRepository(
  private val chapterEnDao: ChapterEnDao,
  private val chapterZhDao: ChapterZhDao,
) {

  /**
   *
   */
  suspend fun insertChapter(chapterEn: ChapterEn) {
    chapterEnDao.insertChapter(chapterEn)
  }

  suspend fun insertChapter(chapterZh: ChapterZh) {
    chapterZhDao.insertChapter(chapterZh)
  }

  suspend fun deleteAll() {
    chapterEnDao.deleteAll()
  }

  fun findDTOFlowOrderByIndex(
    language: String
  ): Flow<List<ChapterDTO>> {
    return if (language == LanguageConstant.ZH) {
      chapterZhDao.findDTOFlowOrderByIndex()
    } else {
      chapterEnDao.findDTOFlowOrderByIndex()
    }
  }

  fun findChapterDTOFlowByIndex(
    language: String,
    index: Int
  ): Flow<ChapterDTO?> {
    return if (language == LanguageConstant.ZH) {
      chapterZhDao.findChapterDTOFlowByIndex(index)
    } else {
      chapterEnDao.findChapterDTOFlowByIndex(index)
    }
  }

  suspend fun findChapterDTOByIndex(
    language: String,
    index: Int
  ): ChapterDTO? {
    return if (language == LanguageConstant.ZH) {
      chapterZhDao.findChapterDTOByIndex(index)
    } else {
      chapterEnDao.findChapterDTOByIndex(index)
    }
  }

  suspend fun getCount(
    language: String
  ): Int {
    return if (language == LanguageConstant.ZH) {
      chapterZhDao.getCount()
    } else {
      chapterEnDao.getCount()
    }
  }
}