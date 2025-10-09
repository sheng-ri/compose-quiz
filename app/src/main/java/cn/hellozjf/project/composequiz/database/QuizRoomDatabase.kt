package cn.hellozjf.project.composequiz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.hellozjf.project.composequiz.database.dao.ChapterDao
import cn.hellozjf.project.composequiz.database.dao.QuizDao
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.Quiz

@Database(entities = [Chapter::class, Quiz::class], version = 4, exportSchema = false)
abstract class QuizRoomDatabase : RoomDatabase() {

  abstract fun quizDao(): QuizDao

  abstract fun chapterDao(): ChapterDao

  companion object {
    private var INSTANCE: QuizRoomDatabase? = null

    fun getInstance(context: Context): QuizRoomDatabase {
      synchronized(this) {
        var instance = INSTANCE
        if (instance == null) {
          instance = Room.databaseBuilder(
            context.applicationContext,
            QuizRoomDatabase::class.java,
            "quiz_database"
          )
            // TODO 这里如果数据库版本变化，会销毁所有数据，所以后面记得把它改掉
            .fallbackToDestructiveMigration()
            .build()
          INSTANCE = instance
        }
        return instance
      }
    }
  }
}