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

    // 2. 安全地获取依赖实例
    // TODO 需要增加一个启动页，等待初始化完成再跳转到主界面

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