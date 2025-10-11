package cn.hellozjf.project.composequiz.database.repository

import androidx.lifecycle.MutableLiveData
import cn.hellozjf.project.composequiz.database.dao.ChapterDao
import cn.hellozjf.project.composequiz.database.entity.Chapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChapterRepository(private val chapterDao: ChapterDao) {
  //  val allProducts: LiveData<List<Product>> = productDao.getAllProducts()
  val searchResults = MutableLiveData<List<Chapter>>()
  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  fun insertChapter(chapter: Chapter) {
    coroutineScope.launch(Dispatchers.IO) {
      chapterDao.insertChapter(chapter)
    }
  }

  fun deleteAll() {
    coroutineScope.launch(Dispatchers.IO) {
      chapterDao.deleteAll()
    }
  }

  fun findAllOrderByIndex(): Flow<List<Chapter>> {
    return chapterDao.findAllOrderByIndex()
  }

  fun getCount(): Int {
    return chapterDao.getCount()
  }
}