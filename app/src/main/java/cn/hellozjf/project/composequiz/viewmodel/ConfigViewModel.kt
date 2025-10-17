package cn.hellozjf.project.composequiz.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.QuizRoomDatabase
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.database.repository.ConfigRepository
import cn.hellozjf.project.composequiz.util.LanguageConstant
import kotlinx.coroutines.flow.Flow

class ConfigViewModel(application: Application) : ViewModel() {

  private val repository: ConfigRepository

  init {
    val quizDb = QuizRoomDatabase.getInstance(application)
    val configDao = quizDb.configDao()
    repository = ConfigRepository(configDao)
  }

  suspend fun updateConfig(config: Config) {
    repository.updateConfig(config)
  }

  suspend fun toggleLanguage() {
    val config = getConfig()
    updateConfig(
      Config(
        language = if (config.language == LanguageConstant.EN) {
          LanguageConstant.ZH
        } else {
          LanguageConstant.EN
        }
      )
    )
  }

  suspend fun getConfig(): Config {
    return repository.getConfig()
  }

  fun getConfigFlow(): Flow<Config> {
    return repository.getConfigFlow()
  }
}