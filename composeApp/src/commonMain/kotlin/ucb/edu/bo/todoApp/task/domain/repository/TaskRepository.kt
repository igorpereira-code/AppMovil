package ucb.edu.bo.todoApp.task.domain.repository

import ucb.edu.bo.todoApp.task.domain.model.TaskModel

interface TaskRepository {
    suspend fun getAll(): List<TaskModel>
    suspend fun getById(taskId: Int): TaskModel?
    suspend fun save(task: TaskModel)
    suspend fun update(task: TaskModel)
    suspend fun delete(taskId: Int)
    suspend fun toggleComplete(taskId: Int, isCompleted: Boolean)
}