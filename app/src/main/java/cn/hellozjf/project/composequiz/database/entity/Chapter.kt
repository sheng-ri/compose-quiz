package cn.hellozjf.project.composequiz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 章节实体
 */
@Entity(tableName = "chapter")
class Chapter {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Int = 0

  @ColumnInfo(name = "index")
  var index: Int = 0

  @ColumnInfo(name = "full_title")
  var fullTitle: String = ""

  @ColumnInfo(name = "simple_title")
  var simpleTitle: String = ""

  @ColumnInfo(name = "simple_url")
  var simpleUrl: String = ""

  @ColumnInfo(name = "full_url")
  var fullUrl: String = ""

  constructor()

  constructor(
    index: Int,
    fullTitle: String,
    simpleTitle: String,
    simpleUrl: String,
    fullUrl: String
  ) {
    this.index = index
    this.fullTitle = fullTitle
    this.simpleTitle = simpleTitle
    this.simpleUrl = simpleUrl
    this.fullUrl = fullUrl
  }
}