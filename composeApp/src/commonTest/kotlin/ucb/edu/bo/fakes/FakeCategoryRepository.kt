package ucb.edu.bo.fakes

import ucb.edu.bo.todoApp.category.domain.model.CategoryModel
import ucb.edu.bo.todoApp.category.domain.repository.CategoryRepository

class FakeCategoryRepository : CategoryRepository {
    var shouldFail = false
    var failureMessage = "Category operation failed"
    private val categories = mutableListOf<CategoryModel>()
    private var idCounter = 1

    val categoriesSnapshot: List<CategoryModel> get() = categories.toList()

    fun setCategories(list: List<CategoryModel>) {
        categories.clear()
        categories.addAll(list)
    }

    override suspend fun getAll(): List<CategoryModel> {
        if (shouldFail) throw Exception(failureMessage)
        return categories.toList()
    }

    override suspend fun save(category: CategoryModel) {
        if (shouldFail) throw Exception(failureMessage)
        val withId = if (category.id == 0) category.copy(id = idCounter++) else category
        categories.add(withId)
    }
}