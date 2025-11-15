package cn.hellozjf.project.composequiz.di

import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.hellozjf.project.composequiz.MyApplication
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.dao.ChapterEnDao
import cn.hellozjf.project.composequiz.database.dao.ChapterZhDao
import cn.hellozjf.project.composequiz.database.dao.ConfigDao
import cn.hellozjf.project.composequiz.database.dao.QuizEnDao
import cn.hellozjf.project.composequiz.database.dao.QuizExtDao
import cn.hellozjf.project.composequiz.database.dao.QuizZhDao
import cn.hellozjf.project.composequiz.eventbus.NavigationEvent
import cn.hellozjf.project.composequiz.eventbus.NavigationEventBus
import cn.hellozjf.project.composequiz.util.LanguageConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Singleton

private val TAG = "DatabaseModule"

/**
 * 这个写法表示，生成的 bean 注入到整个 app 作用域范围
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

  /**
   * 提供 QuizRoomDatabase bean
   * 它的入参是 applicationContext，这个参数来源 @HiltAndroidApp 提供的 bean
   */
  @Provides
  @Singleton
  fun provideDatabase(
    application: MyApplication,
    eventBus: NavigationEventBus
  ): QuizRoomDatabase {
    return Room.databaseBuilder(
      application.applicationContext,
      QuizRoomDatabase::class.java,
      "quiz_database"
    )
      .addCallback(
        databaseCallback(
          application = application,
          eventBus = eventBus
        )
      )
      .addMigrations(
        MIGRATION_31_32
      )
      .build()
  }

  @Provides
  @Singleton
  fun provideQuizEnDao(database: QuizRoomDatabase): QuizEnDao {
    return database.quizEnDao()
  }

  @Provides
  @Singleton
  fun provideQuizZhDao(database: QuizRoomDatabase): QuizZhDao {
    return database.quizZhDao()
  }

  @Provides
  @Singleton
  fun provideQuizExtDao(database: QuizRoomDatabase): QuizExtDao {
    return database.quizExtDao()
  }

  @Provides
  @Singleton
  fun provideChapterEnDao(database: QuizRoomDatabase): ChapterEnDao {
    return database.chapterEnDao()
  }

  @Provides
  @Singleton
  fun provideChapterZhDao(database: QuizRoomDatabase): ChapterZhDao {
    return database.chapterZhDao()
  }

  @Provides
  @Singleton
  fun provideConfigDao(database: QuizRoomDatabase): ConfigDao {
    return database.configDao()
  }

  private fun databaseCallback(
    application: MyApplication,
    eventBus: NavigationEventBus
  ): RoomDatabase.Callback {
    return object : RoomDatabase.Callback() {

      /**
       * 首次创建数据库时会进行回调
       */
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        application.applicationScope.launch {
          Log.d(TAG, "databaseCallback onCreate")
          try {
            // 先执行数据库插入
            insertInitialData(db)
            Log.d(TAG, "databaseCallback after insertInitialData")
          } catch (e: Exception) {
            Log.e(TAG, "databaseCallback failed: ${e.message}", e)
          }
        }
      }

      /**
       * 每次打开数据库时会进行回调
       */
      override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        application.applicationScope.launch {
          Log.d(TAG, "databaseCallback onOpen")
          // 然后发送事件
          eventBus.sendEvent(NavigationEvent.DatabaseOpened)
          Log.d(TAG, "eventBus.sendEvent")
        }
      }
    }
  }

  private suspend fun insertInitialData(db: SupportSQLiteDatabase) {
    withContext(Dispatchers.IO) {
      // 直接使用传入的数据库连接，避免循环依赖
      db.execSQL(
        "INSERT OR IGNORE INTO config (id, language, lastTestChapterIndex) VALUES (?,?,?)",
        arrayOf<Any?>(1, LanguageConstant.EN, null)
      )
    }
  }

  val MIGRATION_31_32 = object : Migration(31, 32) {
    override fun migrate(db: SupportSQLiteDatabase) {
    }
  }
}
