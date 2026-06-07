// ucb.edu.bo.todoApp.category.domain.usecase.GetAllCategoriesUseCase.kt
package ucb.edu.bo.todoApp.category.domain.usecase

import ucb.edu.bo.todoApp.category.domain.model.CategoryModel
import ucb.edu.bo.todoApp.category.domain.repository.CategoryRepository

class GetAllCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): List<CategoryModel> {
        return repository.getAll()
    }
}