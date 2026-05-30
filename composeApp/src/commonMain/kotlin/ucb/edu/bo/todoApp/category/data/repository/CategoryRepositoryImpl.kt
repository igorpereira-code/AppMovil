// ucb.edu.bo.todoApp.category.data.repository.CategoryRepositoryImpl.kt
package ucb.edu.bo.todoApp.category.data.repository

import ucb.edu.bo.todoApp.category.data.datasource.CategoryLocalDataSource
import ucb.edu.bo.todoApp.category.data.mapper.toEntity
import ucb.edu.bo.todoApp.category.data.mapper.toModel
import ucb.edu.bo.todoApp.category.domain.model.CategoryModel
import ucb.edu.bo.todoApp.category.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val localDataSource: CategoryLocalDataSource
) : CategoryRepository {
    override suspend fun getAll(): List<CategoryModel> {
        return localDataSource.getAll().map { it.toModel() }
    }

    override suspend fun save(category: CategoryModel) {
        localDataSource.insert(category.toEntity())
    }
}