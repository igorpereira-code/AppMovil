package ucb.edu.bo.app.home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ucb.edu.bo.app.home.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE syncStatus = 'PENDING'")
    suspend fun getPendingTasks(): List<TaskEntity>

    @Query("UPDATE tasks SET syncStatus = 'SYNCED' WHERE id = :taskId")
    suspend fun markTaskAsSynced(taskId: Long)
}
