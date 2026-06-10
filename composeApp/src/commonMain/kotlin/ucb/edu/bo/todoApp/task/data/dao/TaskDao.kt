package ucb.edu.bo.todoApp.task.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ucb.edu.bo.todoApp.task.data.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getAllByUser(userId: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getById(taskId: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE isSynced = 0 AND userId = :userId")
    suspend fun getUnsyncedTasks(userId: String): List<TaskEntity>

    @Query("UPDATE tasks SET isSynced = :isSynced WHERE id = :taskId")
    suspend fun updateSyncStatus(taskId: Int, isSynced: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun delete(taskId: Int)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun toggleComplete(taskId: Int, isCompleted: Boolean)

    @Query("UPDATE tasks SET isSynced = 1 WHERE id = :taskId")
    suspend fun markAsSynced(taskId: Int)
    
    @Query("DELETE FROM tasks WHERE userId = :userId")
    suspend fun clearAllByUser(userId: String)
}
