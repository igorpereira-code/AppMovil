// ucb.edu.bo.todoApp.category.presentation.state.CategoryUIState.kt
package ucb.edu.bo.todoApp.category.presentation.state

import ucb.edu.bo.todoApp.category.domain.model.CategoryModel

data class CategoryUIState(
    val categories: List<CategoryModel> = emptyList(),
    val isLoading: Boolean = false,
    val isCreatingNew: Boolean = false, // false = Choose Category, true = Create Category


    val newCategoryName: String = "",
    val selectedColor: Long = 0xFFCCFF90,
    val selectedIcon: String = "home",
    val saveError: String? = null
)