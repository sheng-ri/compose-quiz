package cn.hellozjf.project.composequiz.database.repository

import cn.hellozjf.project.composequiz.database.dao.PunchDao
import cn.hellozjf.project.composequiz.database.entity.Punch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PunchRepository @Inject constructor(
  private val punchDao: PunchDao
) {

  suspend fun insertPunch(punch: Punch) {
    punchDao.insertPunch(punch)
  }

  suspend fun getByYearMonth(year: Int, month: Int): List<Punch> {
    return punchDao.getByYearMonth(year, month)
  }

  fun getByYearMonthFlow(year: Int, month: Int): Flow<List<Punch>> {
    return punchDao.getByYearMonthFlow(year, month)
  }

  suspend fun exists(year: Int, month: Int, day: Int): Boolean {
    return punchDao.exists(year, month, day)
  }

}