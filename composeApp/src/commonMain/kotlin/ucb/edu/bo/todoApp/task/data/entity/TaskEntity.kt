package ucb.edu.bo.todoApp.task.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "", // NUEVO: Para separar tareas por usuario
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),

    val dateMillis: Long? = null,
    val timeMillisOfDay: Int? = null,
    val priority: Int = 1,
    val isSynced: Boolean = false,
    val categoryId: Int? = null
)
