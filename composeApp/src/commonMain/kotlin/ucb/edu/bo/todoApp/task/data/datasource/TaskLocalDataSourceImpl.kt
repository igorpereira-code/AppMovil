package ucb.edu.bo.todoApp.task.data.datasource

import ucb.edu.bo.todoApp.task.data.dao.TaskDao
import ucb.edu.bo.todoApp.task.data.entity.TaskEntity

class TaskLocalDataSourceImpl (
    private val taskDao: TaskDao
) : TaskLocalDataSource {

    override suspend fun getAll(): List<TaskEntity> = taskDao.getAll()

    override suspend fun insert(task: TaskEntity) = taskDao.insert(task)

    override suspend fun delete(taskId: Int) = taskDao.delete(taskId)

    override suspend fun toggleComplete(taskId: Int, isCompleted: Boolean) =
        taskDao.toggleComplete(taskId, isCompleted)
}