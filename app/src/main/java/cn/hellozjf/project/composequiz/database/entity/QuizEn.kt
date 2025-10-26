package cn.hellozjf.project.composequiz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 问答实体
 */
@Serializable
@Entity(
  tableName = "quiz_en",
  primaryKeys = ["chapter_index", "quiz_index"]
)
data class QuizEn(

  @ColumnInfo(name = "chapter_index")
  val chapterIndex: Int,

  @ColumnInfo(name = "quiz_index")
  val quizIndex: Int,

  @ColumnInfo(name = "question")
  val question: String,

  @ColumnInfo(name = "options")
  val options: List<String>,

  @ColumnInfo(name = "correct_option_index")
  val correctOptionIndex: Int,

  @ColumnInfo(name = "explanation")
  val explanation: String,
)