package cn.hellozjf.project.composequiz.viewmodel

import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.database.entity.Config
import cn.hellozjf.project.composequiz.database.repository.ConfigRepository
import cn.hellozjf.project.composequiz.util.LanguageConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
  private val repository: ConfigRepository
) : ViewModel() {

  suspend fun updateConfig(config: Config) {
    repository.updateConfig(config)
  }

  suspend fun toggleLanguage() {
    val config = getConfig()
    updateConfig(
      config.copy(
        language = if (config.language == LanguageConstant.EN) {
          LanguageConstant.ZH
        } else {
          LanguageConstant.EN
        }
      )
    )
  }

  suspend fun setLastTestChapterIndex(chapterIndex: Int) {
    val config = getConfig()
    updateConfig(
      config.copy(
        lastTestChapterIndex = chapterIndex
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