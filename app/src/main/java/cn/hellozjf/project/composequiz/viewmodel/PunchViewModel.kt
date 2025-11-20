package cn.hellozjf.project.composequiz.viewmodel

import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.entity.Punch
import cn.hellozjf.project.composequiz.database.repository.PunchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.YearMonth
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

  suspend fun exists(year: Int, month: Int, day: Int): Boolean {
    return repository.exists(year, month, day)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  fun getCacheByYearMonthFlow(year: Int, month: Int): Flow<Map<YearMonth, List<Punch>>> {
    val currentYearMonth: YearMonth = YearMonth.of(year, month)
    val prevYearMonth: YearMonth = currentYearMonth.minusMonths(1L)
    val nextYearMonth: YearMonth = currentYearMonth.plusMonths(1L)
    return flow {
      val currentPunches = repository.getByYearMonth(
        currentYearMonth.year,
        currentYearMonth.monthValue
      )
      val prevPunches = repository.getByYearMonth(
        prevYearMonth.year,
        prevYearMonth.monthValue
      )
      val nextPunches = repository.getByYearMonth(
        nextYearMonth.year,
        nextYearMonth.monthValue
      )
      emit(
        mapOf(
          currentYearMonth to currentPunches,
          prevYearMonth to prevPunches,
          nextYearMonth to nextPunches
        )
      )
    }
  }

}