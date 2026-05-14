package ucb.edu.bo.app.home.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucb.edu.bo.app.home.data.local.dao.TaskDao
import ucb.edu.bo.app.home.data.local.mapper.toDomain
import ucb.edu.bo.app.home.data.local.mapper.toEntity
import ucb.edu.bo.app.home.domain.model.Task
import ucb.edu.bo.app.home.domain.repository.HomeRepository

class HomeRepositoryImpl(
    private val taskDao: TaskDao
) : HomeRepository {
    override suspend fun saveTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
