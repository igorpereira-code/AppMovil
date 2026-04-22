package ucb.edu.bo.remoteconfig.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ucb.edu.bo.remoteconfig.data.entity.RemoteConfigEntity

@Dao
interface RemoteConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: RemoteConfigEntity)

    @Query("SELECT * FROM remote_config_cache WHERE `key` = :key LIMIT 1")
    suspend fun getByKey(key: String): RemoteConfigEntity?

    @Query("SELECT * FROM remote_config_cache")
    suspend fun getAll(): List<RemoteConfigEntity>
}