// ucb.edu.bo.todoApp.category.data.mapper.CategoryMapper.kt
package ucb.edu.bo.todoApp.category.data.mapper

import ucb.edu.bo.todoApp.category.data.entity.CategoryEntity
import ucb.edu.bo.todoApp.category.domain.model.CategoryModel

fun CategoryEntity.toModel() = CategoryModel(
    id = id,
    name = name,
    iconResName = iconResName,
    colorHex = colorHex
)

fun CategoryModel.toEntity() = CategoryEntity(
    id = id,
    name = name,
    iconResName = iconResName,
    colorHex = colorHex
)