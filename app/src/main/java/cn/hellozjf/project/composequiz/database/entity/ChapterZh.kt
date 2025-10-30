package cn.hellozjf.project.composequiz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 章节实体
 * todo 把id去掉，index直接当主键用
 */
@Entity(
  tableName = "chapter_zh"
)
data class ChapterZh(

  @PrimaryKey
  @ColumnInfo(name = "index")
  var index: Int = 0,

  @ColumnInfo(name = "full_title")
  var fullTitle: String = "",

  @ColumnInfo(name = "simple_title")
  var simpleTitle: String = "",

  @ColumnInfo(name = "simple_url")
  var simpleUrl: String = "",

  @ColumnInfo(name = "full_url")
  var fullUrl: String = "",
)