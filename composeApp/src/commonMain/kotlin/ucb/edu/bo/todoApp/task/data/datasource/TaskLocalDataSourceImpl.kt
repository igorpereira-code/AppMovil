package ucb.edu.bo.todoApp.task.data.datasource

import ucb.edu.bo.todoApp.task.data.dao.TaskDao
import ucb.edu.bo.todoApp.task.data.entity.TaskEntity

class TaskLocalDataSourceImpl (
    private val taskDao: TaskDao
) : TaskLocalDataSource {

    override suspend fun getAll(userId: String): List<TaskEntity> = taskDao.getAllByUser(userId)

    override suspend fun getById(taskId: Int): TaskEntity? = taskDao.getById(taskId)

    override suspend fun insert(task: TaskEntity) = taskDao.insert(task)

    override suspend fun update(task: TaskEntity) = taskDao.update(task)

    override suspend fun delete(taskId: Int) = taskDao.delete(taskId)

    override suspend fun toggleComplete(taskId: Int, isCompleted: Boolean) =
        taskDao.toggleComplete(taskId, isCompleted)

    override suspend fun markAsSynced(taskId: Int) = taskDao.markAsSynced(taskId)

    override suspend fun clearAllByUser(userId: String) = taskDao.clearAllByUser(userId)
}
