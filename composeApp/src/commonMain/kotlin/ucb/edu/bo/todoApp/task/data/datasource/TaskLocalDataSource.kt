package ucb.edu.bo.todoApp.task.data.datasource

import ucb.edu.bo.todoApp.task.data.entity.TaskEntity

interface TaskLocalDataSource {
    suspend fun getAll(): List<TaskEntity>

    suspend fun getById(taskId: Int): TaskEntity?
    suspend fun insert(task: TaskEntity)

    suspend fun update(task: TaskEntity)
    suspend fun delete(taskId: Int)
    suspend fun toggleComplete(taskId: Int, isCompleted: Boolean)
    suspend fun markAsSynced(taskId: Int)
}