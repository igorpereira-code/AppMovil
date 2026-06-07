// ucb.edu.bo.todoApp.category.domain.model.CategoryModel.kt
package ucb.edu.bo.todoApp.category.domain.model

data class CategoryModel(
    val id: Int = 0,
    val name: String,
    val iconResName: String,
    val colorHex: Long
)