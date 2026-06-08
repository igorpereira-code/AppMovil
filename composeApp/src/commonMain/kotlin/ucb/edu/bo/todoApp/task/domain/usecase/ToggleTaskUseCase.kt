package ucb.edu.bo.todoApp.task.domain.usecase

import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository

class ToggleTaskUseCase (private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int, isCompleted: Boolean): Result<Unit> {
        return try {
            repository.toggleComplete(taskId, isCompleted)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}