package cn.hellozjf.project.composequiz.database.repository

import cn.hellozjf.project.composequiz.database.dao.ConfigDao
import cn.hellozjf.project.composequiz.database.entity.Config
import kotlinx.coroutines.flow.Flow

class ConfigRepository(private val configDao: ConfigDao) {

  suspend fun insertConfig(config: Config) {
    configDao.insertConfig(config)
  }

  suspend fun deleteConfig(config: Config) {
    configDao.deleteConfig(config)
  }

  suspend fun updateConfig(config: Config) {
    configDao.updateConfig(config)
  }

  suspend fun getConfig(): Config? {
    return configDao.getConfig()
  }

  fun getConfigFlow(): Flow<Config?> {
    return configDao.getConfigFlow()
  }
}