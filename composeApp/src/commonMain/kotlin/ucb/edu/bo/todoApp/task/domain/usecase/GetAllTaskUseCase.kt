package ucb.edu.bo.todoApp.task.domain.usecase

import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository

class GetAllTaskUseCase (
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): List<TaskModel> {
        return repository.getAll()
    }
}