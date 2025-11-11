package cn.hellozjf.project.composequiz

import android.app.Application
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class MyApplication : Application() {

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface DatabaseEntryPoint {
    fun quizRoomDatabase(): QuizRoomDatabase
  }

  override fun onCreate() {
    super.onCreate()

    // 2. 安全地获取依赖实例
    // TODO 需要增加一个启动页，等待初始化完成再跳转到主界面
    val hiltEntryPoint = EntryPoints.get(this, DatabaseEntryPoint::class.java)
    val database = hiltEntryPoint.quizRoomDatabase()

    // 在这里进行全局初始化
    // 初始化日志库
    // 初始化数据库
    // 初始化后台任务
  }
}