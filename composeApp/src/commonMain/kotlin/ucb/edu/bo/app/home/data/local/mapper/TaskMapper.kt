package ucb.edu.bo.app.home.data.local.mapper

import ucb.edu.bo.app.home.data.local.entity.TaskEntity
import ucb.edu.bo.app.home.domain.model.Task

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        title = title,
        description = description,
        time = time,
        category = category,
        priority = priority,
        isCompleted = isCompleted
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        title = title,
        description = description,
        time = time,
        category = category,
        priority = priority,
        isCompleted = isCompleted
    )
}
