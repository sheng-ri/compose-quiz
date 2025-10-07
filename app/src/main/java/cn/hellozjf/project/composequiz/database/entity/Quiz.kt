package cn.hellozjf.project.composequiz.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 问答实体
 */
@Entity(tableName = "quiz")
class Quiz {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  var id: Int = 0

  @ColumnInfo(name = "chapter")
  var chapter: Int = 0

  @ColumnInfo(name = "question")
  var question: String = ""

  @ColumnInfo(name = "correct_option")
  var correctOption: String = ""

  @ColumnInfo(name = "other_option1")
  var otherOption1: String = ""

  @ColumnInfo(name = "other_option2")
  var otherOption2: String = ""

  @ColumnInfo(name = "other_option3")
  var otherOption3: String = ""

  @ColumnInfo(name = "explanation")
  var explanation: String = ""

  constructor()

  constructor(
    chapter: Int,
    question: String,
    correctOption: String,
    otherOptions: List<String>,
    explanation: String
  ) {
    this.chapter = chapter
    this.question = question
    this.correctOption = correctOption
    if (otherOptions.size == 3) {
      this.otherOption1 = otherOptions[0]
      this.otherOption2 = otherOptions[1]
      this.otherOption3 = otherOptions[2]
    }
    this.explanation = explanation
  }
}