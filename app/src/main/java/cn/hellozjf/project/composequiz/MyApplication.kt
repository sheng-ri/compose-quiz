package cn.hellozjf.project.composequiz

import android.app.Application
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase

class MyApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    // 在这里进行全局初始化
    // 初始化日志库
    // 初始化数据库
    initDatabase()
    // 初始化后台任务
  }

  private fun initDatabase() {
    // 同步初始化，确保完成后再继续
    QuizRoomDatabase.getInstance(applicationContext)
  }
}