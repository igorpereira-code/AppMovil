// ucb.edu.bo.todoApp.category.domain.repository.CategoryRepository.kt
package ucb.edu.bo.todoApp.category.domain.repository

import ucb.edu.bo.todoApp.category.domain.model.CategoryModel

interface CategoryRepository {
    suspend fun getAll(): List<CategoryModel>
    suspend fun save(category: CategoryModel)
}