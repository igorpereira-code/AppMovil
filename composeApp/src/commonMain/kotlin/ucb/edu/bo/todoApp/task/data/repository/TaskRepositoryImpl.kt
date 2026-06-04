package ucb.edu.bo.todoApp.task.data.repository

import ucb.edu.bo.todoApp.task.data.datasource.TaskLocalDataSource
import ucb.edu.bo.todoApp.task.data.mapper.toEntity
import ucb.edu.bo.todoApp.task.data.mapper.toModel
import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository

class TaskRepositoryImpl (
    private val localDataSource: TaskLocalDataSource
) : TaskRepository {

    override suspend fun getAll(): List<TaskModel> {
        return localDataSource.getAll().map { it.toModel() }
    }

    override suspend fun getById(taskId: Int): TaskModel? {
        return localDataSource.getById(taskId)?.toModel()
    }

    override suspend fun save(task: TaskModel) {
        localDataSource.insert(task.toEntity())
    }

    override suspend fun update(task: TaskModel) {
        localDataSource.update(task.toEntity())
    }

    override suspend fun delete(taskId: Int) {
        localDataSource.delete(taskId)
    }

    override suspend fun toggleComplete(taskId: Int, isCompleted: Boolean) {
        localDataSource.toggleComplete(taskId, isCompleted)
    }
}