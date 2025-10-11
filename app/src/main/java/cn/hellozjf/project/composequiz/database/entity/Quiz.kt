package cn.hellozjf.project.composequiz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 问答实体
 */
@Serializable
@Entity(tableName = "quiz")
data class Quiz(

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Int = 0,

  @ColumnInfo(name = "chapter_index")
  val chapterIndex: Int,

  @ColumnInfo(name = "question")
  val question: String,

  @ColumnInfo(name = "correct_option")
  val correctOption: String,

  @ColumnInfo(name = "wrong_option1")
  val wrongOption1: String,

  @ColumnInfo(name = "wrong_option2")
  val wrongOption2: String,

  @ColumnInfo(name = "wrong_option3")
  val wrongOption3: String,

  @ColumnInfo(name = "explanation")
  val explanation: String,

  @ColumnInfo(name = "favorite")
  val favorite: Boolean = false,

  @ColumnInfo(name = "favorite_time")
  val favoriteTime: Long = 0L,

  @ColumnInfo(name = "wrong_answer_count")
  val wrongAnswerCount: Int = 0
)