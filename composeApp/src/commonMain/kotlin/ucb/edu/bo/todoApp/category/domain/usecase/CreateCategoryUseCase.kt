// ucb.edu.bo.todoApp.category.domain.usecase.CreateCategoryUseCase.kt
package ucb.edu.bo.todoApp.category.domain.usecase

import ucb.edu.bo.todoApp.category.domain.model.CategoryModel
import ucb.edu.bo.todoApp.category.domain.repository.CategoryRepository

class CreateCategoryUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: CategoryModel): Result<Unit> {
        if (category.name.isBlank()) return Result.failure(Exception("El nombre de la categoría no puede estar vacío"))
        return try {
            repository.save(category)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}