package ucb.edu.bo.todoApp.task.domain.usecase

import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository

class UpdateTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: TaskModel): Result<Unit> {
        if (task.title.isBlank()) return Result.failure(Exception("El título no puede estar vacío"))
        return try {
            repository.update(task)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
