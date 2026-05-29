// ucb.edu.bo.todoApp.category.data.datasource.CategoryLocalDataSource.kt
package ucb.edu.bo.todoApp.category.data.datasource

import ucb.edu.bo.todoApp.category.data.entity.CategoryEntity

interface CategoryLocalDataSource {
    suspend fun getAll(): List<CategoryEntity>
    suspend fun insert(category: CategoryEntity)
}