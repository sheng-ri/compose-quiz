package cn.hellozjf.project.composequiz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.Serializable

/**
 * 打卡日期
 */

@Serializable
@Entity(
  tableName = "punch",
  primaryKeys = ["year", "month", "day"]
)
data class Punch(

  /**
   * 年
   */
  @ColumnInfo(name = "year")
  val year: Int,

  /**
   * 月
   */
  @ColumnInfo(name = "month")
  val month: Int,

  /**
   * 日
   */
  @ColumnInfo(name = "day")
  val day: Int
)