package ucb.edu.bo.app.home.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val time: String,
    val category: String,
    val priority: Int,
    val isCompleted: Boolean,
    val syncStatus: String = "PENDING" // PENDING, SYNCED
)
