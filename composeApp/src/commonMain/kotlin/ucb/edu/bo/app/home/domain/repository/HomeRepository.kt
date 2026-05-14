package ucb.edu.bo.app.home.domain.repository

import kotlinx.coroutines.flow.Flow
import ucb.edu.bo.app.home.domain.model.Task

interface HomeRepository {
    suspend fun saveTask(task: Task)
    fun getTasks(): Flow<List<Task>>
}
