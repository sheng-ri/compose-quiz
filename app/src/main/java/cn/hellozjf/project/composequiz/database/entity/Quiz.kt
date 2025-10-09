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

  @ColumnInfo(name = "chapter_index")
  var chapterIndex: Int = 0

  @ColumnInfo(name = "question")
  var question: String = ""

  @ColumnInfo(name = "correct_option")
  var correctOption: String = ""

  @ColumnInfo(name = "wrong_option1")
  var wrongOption1: String = ""

  @ColumnInfo(name = "wrong_option2")
  var wrongOption2: String = ""

  @ColumnInfo(name = "wrong_option3")
  var wrongOption3: String = ""

  @ColumnInfo(name = "explanation")
  var explanation: String = ""

  constructor()

  constructor(
    chapterIndex: Int,
    question: String,
    correctOption: String,
    wrongOptions: List<String>,
    explanation: String
  ) {
    this.chapterIndex = chapterIndex
    this.question = question
    this.correctOption = correctOption
    if (wrongOptions.size == 3) {
      this.wrongOption1 = wrongOptions[0]
      this.wrongOption2 = wrongOptions[1]
      this.wrongOption3 = wrongOptions[2]
    }
    this.explanation = explanation
  }
}