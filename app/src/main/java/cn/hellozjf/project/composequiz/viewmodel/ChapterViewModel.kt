package cn.hellozjf.project.composequiz.viewmodel

import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.repository.ChapterRepository
import cn.hellozjf.project.composequiz.dto.ChapterDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
  private val repository: ChapterRepository
) : ViewModel() {

  suspend fun insertChapter(chapterEn: ChapterEn) {
    repository.insertChapter(chapterEn)
  }

  suspend fun insertChapter(chapterZh: ChapterZh) {
    repository.insertChapter(chapterZh)
  }

  fun findDTOFlowOrderByIndex(
    language: String
  ): Flow<List<ChapterDTO>> {
    return repository.findDTOFlowOrderByIndex(language)
  }

  fun findChapterDTOFlowByIndex(
    language: String,
    index: Int
  ): Flow<ChapterDTO?> {
    return repository.findChapterDTOFlowByIndex(
      language = language,
      index = index
    )
  }

  suspend fun findChapterDTOByIndex(
    language: String,
    index: Int
  ): ChapterDTO? {
    return repository.findChapterDTOByIndex(
      language = language,
      index = index
    )
  }

  suspend fun getCount(
    language: String
  ): Int {
    return repository.getCount(
      language = language
    )
  }

  suspend fun deleteAll() {
    repository.deleteAll()
  }
}
