package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.hellozjf.project.composequiz.database.entity.Punch
import kotlinx.coroutines.flow.Flow

@Dao
interface PunchDao {

  @Insert
  suspend fun insertPunch(punch: Punch)

  @Query("SELECT * FROM punch WHERE year = :year AND month = :month")
  suspend fun getByYearMonth(year: Int, month: Int): List<Punch>

  @Query("SELECT * FROM punch WHERE year = :year AND month = :month")
  fun getByYearMonthFlow(year: Int, month: Int): Flow<List<Punch>>

  @Query("SELECT EXISTS(SELECT 1 FROM punch WHERE year = :year and month = :month and day = :day)")
  suspend fun exists(year: Int, month: Int, day: Int): Boolean

}