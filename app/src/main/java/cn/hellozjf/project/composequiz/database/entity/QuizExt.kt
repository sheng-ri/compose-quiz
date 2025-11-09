package cn.hellozjf.project.composequiz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 问答实体额外字段
 */
@Serializable
@Entity(
  tableName = "quiz_ext",
  primaryKeys = ["chapter_index", "quiz_index"]
)
data class QuizExt(

  @ColumnInfo(name = "chapter_index")
  val chapterIndex: Int,

  @ColumnInfo(name = "quiz_index")
  val quizIndex: Int,

  @ColumnInfo(name = "favorite")
  val favorite: Boolean = false,

  @ColumnInfo(name = "favorite_time")
  val favoriteTime: Long = 0L,

  @ColumnInfo(name = "wrong_answer_count")
  val wrongAnswerCount: Int = 0,

  @ColumnInfo(name = "description")
  val description: String,
)