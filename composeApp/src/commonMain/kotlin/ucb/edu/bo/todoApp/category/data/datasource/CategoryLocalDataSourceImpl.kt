// ucb.edu.bo.todoApp.category.data.datasource.CategoryLocalDataSourceImpl.kt
package ucb.edu.bo.todoApp.category.data.datasource

import ucb.edu.bo.todoApp.category.data.dao.CategoryDao
import ucb.edu.bo.todoApp.category.data.entity.CategoryEntity

class CategoryLocalDataSourceImpl(
    private val categoryDao: CategoryDao
) : CategoryLocalDataSource {
    override suspend fun getAll(): List<CategoryEntity> = categoryDao.getAll()
    override suspend fun insert(category: CategoryEntity) = categoryDao.insert(category)
}