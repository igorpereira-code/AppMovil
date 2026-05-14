package ucb.edu.bo.app.home.domain.usecase

import kotlinx.coroutines.flow.Flow
import ucb.edu.bo.app.home.domain.model.Task
import ucb.edu.bo.app.home.domain.repository.HomeRepository

class GetTasksUseCase(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.getTasks()
    }
}
