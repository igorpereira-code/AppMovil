package ucb.edu.bo.todoApp.task.domain.usecase

import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository

class DeleteTaskUseCase (
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int): Result<Unit> {
        return try {
            repository.delete(taskId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}