package cn.hellozjf.project.composequiz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.hellozjf.project.composequiz.database.dao.ChapterEnDao
import cn.hellozjf.project.composequiz.database.dao.ChapterZhDao
import cn.hellozjf.project.composequiz.database.dao.ConfigDao
import cn.hellozjf.project.composequiz.database.dao.QuizEnDao
import cn.hellozjf.project.composequiz.database.dao.QuizExtDao
import cn.hellozjf.project.composequiz.database.dao.QuizZhDao
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizExt
import cn.hellozjf.project.composequiz.database.entity.QuizZh
import cn.hellozjf.project.composequiz.util.LanguageConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    ChapterEn::class,
    ChapterZh::class,
    QuizEn::class,
    QuizZh::class,
    QuizExt::class,
    Config::class
  ], version = 25, exportSchema = false
)
abstract class QuizRoomDatabase : RoomDatabase() {

  abstract fun quizEnDao(): QuizEnDao
  abstract fun quizZhDao(): QuizZhDao
  abstract fun quizExtDao(): QuizExtDao

  abstract fun chapterEnDao(): ChapterEnDao
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
            .addCallback(object : RoomDatabase.Callback() {
              override fun onCreate(db: SupportSQLiteDatabase) {
                // 只在首次创建数据库时执行
                insertInitialData() // 版本升级时不会执行！
              }

              override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                insertInitialData()
              }

              private fun insertInitialData() {
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                  val database = QuizRoomDatabase.getInstance(context)
                  // 重新插入初始数据
                  database.configDao().insertConfig(
                    Config(language = LanguageConstant.EN)
                  )
                }
              }
            })
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