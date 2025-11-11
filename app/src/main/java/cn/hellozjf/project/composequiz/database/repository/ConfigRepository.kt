package cn.hellozjf.project.composequiz.database.repository

import cn.hellozjf.project.composequiz.database.dao.ConfigDao
import cn.hellozjf.project.composequiz.database.entity.Config
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
  private val configDao: ConfigDao
) {

  suspend fun updateConfig(config: Config) {
    configDao.updateConfig(config)
  }

  suspend fun getConfig(): Config {
    return configDao.getConfig()
  }

  fun getConfigFlow(): Flow<Config> {
    return configDao.getConfigFlow()
  }
}