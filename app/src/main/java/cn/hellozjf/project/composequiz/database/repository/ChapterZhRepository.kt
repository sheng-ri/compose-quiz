package cn.hellozjf.project.composequiz.database.repository

import androidx.lifecycle.MutableLiveData
import cn.hellozjf.project.composequiz.database.dao.ChapterDao
import cn.hellozjf.project.composequiz.database.dao.ChapterZhDao
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChapterZhRepository(private val chapterDao: ChapterZhDao) {
  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  fun insertChapter(chapter: ChapterZh) {
    coroutineScope.launch(Dispatchers.IO) {
      chapterDao.insertChapter(chapter)
    }
  }

  fun deleteAll() {
    coroutineScope.launch(Dispatchers.IO) {
      chapterDao.deleteAll()
    }
  }

  fun findAllOrderByIndex(): Flow<List<ChapterZh>> {
    return chapterDao.findAllOrderByIndex()
  }

  fun findByIndexFlow(index: Int): Flow<List<ChapterZh>> {
    return chapterDao.findByIndexFlow(index)
  }

  suspend fun findByIndex(index: Int): ChapterZh? {
    return chapterDao.findByIndex(index)
  }

  fun getCount(): Int {
    return chapterDao.getCount()
  }
}