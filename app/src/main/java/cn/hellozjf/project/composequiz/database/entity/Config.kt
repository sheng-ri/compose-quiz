package cn.hellozjf.project.composequiz.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 配置类，存储一些 App 中的公有数据，例如：语言
 */
@Entity(
  tableName = "config"
)
data class Config(
  @PrimaryKey
  val id: Int = 1,
  val language: String
)