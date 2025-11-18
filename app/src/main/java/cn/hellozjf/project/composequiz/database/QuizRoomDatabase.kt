package cn.hellozjf.project.composequiz.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cn.hellozjf.project.composequiz.database.converter.Converters
import cn.hellozjf.project.composequiz.database.dao.ChapterEnDao
import cn.hellozjf.project.composequiz.database.dao.ChapterZhDao
import cn.hellozjf.project.composequiz.database.dao.ConfigDao
import cn.hellozjf.project.composequiz.database.dao.PunchDao
import cn.hellozjf.project.composequiz.database.dao.QuizEnDao
import cn.hellozjf.project.composequiz.database.dao.QuizExtDao
import cn.hellozjf.project.composequiz.database.dao.QuizZhDao
import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.database.entity.ChapterZh
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.database.entity.Punch
import cn.hellozjf.project.composequiz.database.entity.QuizEn
import cn.hellozjf.project.composequiz.database.entity.QuizExt
import cn.hellozjf.project.composequiz.database.entity.QuizZh

private val TAG = "QuizRoomDatabase"

@Database(
  entities = [
    ChapterEn::class,
    ChapterZh::class,
    QuizEn::class,
    QuizZh::class,
    QuizExt::class,
    Config::class,
    Punch::class
  ], version = 33, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuizRoomDatabase : RoomDatabase() {

  abstract fun quizEnDao(): QuizEnDao
  abstract fun quizZhDao(): QuizZhDao
  abstract fun quizExtDao(): QuizExtDao

  abstract fun chapterEnDao(): ChapterEnDao
  abstract fun chapterZhDao(): ChapterZhDao

  abstract fun configDao(): ConfigDao

  abstract fun punchDao(): PunchDao
}