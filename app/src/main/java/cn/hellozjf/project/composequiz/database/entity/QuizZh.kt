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
  tableName = "quiz_zh",
  primaryKeys = ["chapter_index", "quiz_index"]
)
data class QuizZh(

  // 这个是书中实际的章节号
  @ColumnInfo(name = "chapter_index")
  val chapterIndex: Int,

  // 这个是问题序号，从0开始
  @ColumnInfo(name = "quiz_index")
  val quizIndex: Int,

  @ColumnInfo(name = "question")
  val question: String,

  @ColumnInfo(name = "options")
  val options: List<String>,

  // 这个是选项序号，从0开始
  @ColumnInfo(name = "correct_option_index")
  val correctOptionIndex: Int,

  @ColumnInfo(name = "explanation")
  val explanation: String,
)