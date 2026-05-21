package ucb.edu.bo.todoApp.task.data.mapper

import ucb.edu.bo.todoApp.task.data.entity.TaskEntity
import ucb.edu.bo.todoApp.task.domain.model.TaskModel

fun TaskEntity.toModel() = TaskModel(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt
)

fun TaskModel.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt
)