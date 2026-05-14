package ucb.edu.bo.app.home.domain.usecase

import ucb.edu.bo.app.home.domain.model.Task
import ucb.edu.bo.app.home.domain.repository.HomeRepository

class SaveTaskUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.saveTask(task)
    }
}
