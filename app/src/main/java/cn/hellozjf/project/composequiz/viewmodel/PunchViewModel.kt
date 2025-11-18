package cn.hellozjf.project.composequiz.viewmodel

import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.entity.Punch
import cn.hellozjf.project.composequiz.database.repository.PunchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PunchViewModel @Inject constructor(
  private val repository: PunchRepository
) : ViewModel() {

  suspend fun insertPunch(punch: Punch) {
    repository.insertPunch(punch)
  }

  suspend fun getByYearMonth(year: Int, month: Int): List<Punch> {
    return repository.getByYearMonth(year, month)
  }

  fun getByYearMonthFlow(year: Int, month: Int): Flow<List<Punch>> {
    return repository.getByYearMonthFlow(year, month)
  }

}