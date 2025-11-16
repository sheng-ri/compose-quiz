package cn.hellozjf.project.composequiz

import android.app.Application
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

  val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

  @Inject
  lateinit var database: QuizRoomDatabase

  override fun onCreate() {
    super.onCreate()

    // 在这里进行全局初始化
    // 初始化日志库
    // 初始化数据库
    // 初始化后台任务
  }

  override fun onTerminate() {
    applicationScope.cancel()
    super.onTerminate()
  }
}