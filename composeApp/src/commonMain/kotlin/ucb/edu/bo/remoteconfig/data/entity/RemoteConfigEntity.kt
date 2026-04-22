package ucb.edu.bo.remoteconfig.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_config_cache")
data class RemoteConfigEntity(
    @PrimaryKey
    val key: String,
    val value: String,
    val updatedAt: Long = System.currentTimeMillis()
)