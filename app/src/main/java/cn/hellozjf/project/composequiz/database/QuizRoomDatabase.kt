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
}