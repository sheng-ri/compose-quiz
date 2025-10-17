package cn.hellozjf.project.composequiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.hellozjf.project.composequiz.database.entity.Config
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {

  @Insert
  suspend fun insertConfig(config: Config)

  @Update
  suspend fun updateConfig(config: Config)

  @Query("SELECT * FROM config WHERE id = 1")
  suspend fun getConfig(): Config

  @Query("SELECT * FROM config WHERE id = 1")
  fun getConfigFlow(): Flow<Config>
}