package ucb.edu.bo.config.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_configs")
data class ConfigEntity(
    @PrimaryKey
    @ColumnInfo(name = "config_key")
    val key: String,

    @ColumnInfo(name = "config_value")
    val value: String
)
