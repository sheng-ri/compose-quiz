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

  /**
   * 当前题目的语言
   */
  val language: String,

  /**
   * 上一次测试的章节序号
   */
  val lastTestChapterIndex: Int? = null
)