package cn.hellozjf.project.composequiz.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.dao.ChapterEnDao
import cn.hellozjf.project.composequiz.database.dao.ChapterZhDao
import cn.hellozjf.project.composequiz.database.dao.ConfigDao
import cn.hellozjf.project.composequiz.database.dao.QuizEnDao
import cn.hellozjf.project.composequiz.database.dao.QuizExtDao
import cn.hellozjf.project.composequiz.database.dao.QuizZhDao
import cn.hellozjf.project.composequiz.util.LanguageConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

private val TAG = "DatabaseModule"

/**
 * 这个写法表示，生成的 bean 注入到整个 app 作用域范围
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

  /**
   * 提供 QuizRoomDatabase bean
   * 它的入参是 applicationContext，这个参数来源 @HiltAndroidApp 提供的 bean
   */
  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext context: Context): QuizRoomDatabase {
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

  @Provides
  fun provideQuizEnDao(database: QuizRoomDatabase): QuizEnDao {
    return database.quizEnDao()
  }

  @Provides
  fun provideQuizZhDao(database: QuizRoomDatabase): QuizZhDao {
    return database.quizZhDao()
  }

  @Provides
  fun provideQuizExtDao(database: QuizRoomDatabase): QuizExtDao {
    return database.quizExtDao()
  }

  @Provides
  fun provideChapterEnDao(database: QuizRoomDatabase): ChapterEnDao {
    return database.chapterEnDao()
  }

  @Provides
  fun provideChapterZhDao(database: QuizRoomDatabase): ChapterZhDao {
    return database.chapterZhDao()
  }

  @Provides
  fun provideConfigDao(database: QuizRoomDatabase): ConfigDao {
    return database.configDao()
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