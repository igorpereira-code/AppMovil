package ucb.edu.bo.config.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ucb.edu.bo.config.data.entity.ConfigEntity

@Dao
interface ConfigDao {
    @Query("SELECT * FROM remote_configs WHERE config_key = :key LIMIT 1")
    suspend fun getConfig(key: String): ConfigEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ConfigEntity)
}