package cn.hellozjf.project.composequiz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.hellozjf.project.composequiz.database.dao.ChapterDao
import cn.hellozjf.project.composequiz.database.dao.ChapterZhDao
import cn.hellozjf.project.composequiz.database.dao.ConfigDao
import cn.hellozjf.project.composequiz.database.dao.QuizEnDao
import cn.hellozjf.project.composequiz.database.dao.QuizExtDao
import cn.hellozjf.project.composequiz.database.dao.QuizZhDao
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizExt
import cn.hellozjf.project.composequiz.database.entity.QuizZh

@Database(
  entities = [
    Chapter::class,
    ChapterZh::class,
    QuizEn::class,
    QuizZh::class,
    QuizExt::class,
    Config::class
  ], version = 13, exportSchema = false
)
abstract class QuizRoomDatabase : RoomDatabase() {

  abstract fun quizEnDao(): QuizEnDao
  abstract fun quizZhDao(): QuizZhDao
  abstract fun quizExtDao(): QuizExtDao

  abstract fun chapterDao(): ChapterDao
  abstract fun chapterZhDao(): ChapterZhDao

  abstract fun configDao(): ConfigDao

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