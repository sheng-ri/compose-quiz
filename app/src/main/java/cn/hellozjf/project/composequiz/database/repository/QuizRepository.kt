package cn.hellozjf.project.composequiz.database.repository

import androidx.lifecycle.MutableLiveData
import cn.hellozjf.project.composequiz.database.dao.QuizDao
import cn.hellozjf.project.composequiz.database.entity.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class QuizRepository(private val quizDao: QuizDao) {
  //  val allProducts: LiveData<List<Product>> = productDao.getAllProducts()
  val searchResults = MutableLiveData<List<Quiz>>()
  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  fun insertQuiz(quiz: Quiz) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.insertQuiz(quiz)
    }
  }

  fun deleteQuizByChapter(chapter: Int) {
    coroutineScope.launch(Dispatchers.IO) {
      quizDao.deleteByChapter(chapter)
    }
  }

  fun findQuizByChapter(chapter: Int) {
    coroutineScope.launch(Dispatchers.Main) {
      searchResults.value = asyncFind(chapter).await()
    }
  }

  private fun asyncFind(chapter: Int): Deferred<List<Quiz>?> =
    coroutineScope.async(Dispatchers.IO) {
      return@async quizDao.findByChapter(chapter)
    }
}