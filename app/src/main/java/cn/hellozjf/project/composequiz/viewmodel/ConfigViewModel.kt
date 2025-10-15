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

  suspend fun insertConfig(config: Config) {
    repository.insertConfig(config)
  }

  suspend fun deleteConfig(config: Config) {
    repository.deleteConfig(config)
  }

  suspend fun updateConfig(config: Config) {
    repository.updateConfig(config)
  }

  suspend fun toggleLanguage() {
    val config = getConfig()
    config?.let {
      updateConfig(Config(
        language = if (it.language == LanguageConstant.EN) {
          LanguageConstant.ZH
        } else {
          LanguageConstant.EN
        }
      ))
    } ?: run {
      insertConfig(Config(
        language = LanguageConstant.EN
      ))
    }
  }

  suspend fun getConfig(): Config? {
    return repository.getConfig()
  }

  fun getConfigFlow(): Flow<Config?> {
    return repository.getConfigFlow()
  }
}