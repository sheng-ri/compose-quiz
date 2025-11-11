package cn.hellozjf.project.composequiz.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.hellozjf.project.composequiz.database.converter.Converters
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

private val TAG = "QuizRoomDatabase"

@Database(
  entities = [
    ChapterEn::class,
    ChapterZh::class,
    QuizEn::class,
    QuizZh::class,
    QuizExt::class,
    Config::class
  ], version = 32, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuizRoomDatabase : RoomDatabase() {

  abstract fun quizEnDao(): QuizEnDao
  abstract fun quizZhDao(): QuizZhDao
  abstract fun quizExtDao(): QuizExtDao

  abstract fun chapterEnDao(): ChapterEnDao
  abstract fun chapterZhDao(): ChapterZhDao

  abstract fun configDao(): ConfigDao

  companion object {
    @Volatile
    private var INSTANCE: QuizRoomDatabase? = null

    fun getInstance(context: Context): QuizRoomDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
      }
    }

    private fun buildDatabase(context: Context): QuizRoomDatabase {
      return Room.databaseBuilder(
        context.applicationContext,
        QuizRoomDatabase::class.java,
        "quiz_database"
      )
        .addCallback(databaseCallback(context))
        .addMigrations(
          MIGRATION_31_32
        )
        .build()
    }

    private fun databaseCallback(context: Context): RoomDatabase.Callback {
      return object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
          super.onCreate(db)
          Log.d(TAG, "databaseCallback onCreate")
          insertInitialData(db)
          Log.d(TAG, "databaseCallback after insertInitialData")
        }
      }
    }

    private fun insertInitialData(db: SupportSQLiteDatabase) {
      CoroutineScope(Dispatchers.IO).launch {
        // 直接使用传入的数据库连接，避免循环依赖
        db.query(
          "INSERT INTO config (language) VALUES (?)",
          arrayOf(LanguageConstant.EN)
        )
      }
    }

    val MIGRATION_31_32 = object : Migration(31, 32) {
      override fun migrate(db: SupportSQLiteDatabase) {
      }
    }
  }
}