package ucb.edu.bo.todoApp.task.data.mapper

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import ucb.edu.bo.todoApp.task.data.entity.TaskEntity
import ucb.edu.bo.todoApp.task.domain.model.TaskModel

fun TaskEntity.toModel() = TaskModel(
    id = id,
    userId = userId, // Mapeo del nuevo campo
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt,

    // Convertimos los primitivos de la BD a objetos kotlinx-datetime
    date = dateMillis?.let {
        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
    },
    time = timeMillisOfDay?.let {
        LocalTime.fromMillisecondOfDay(it)
    },
    priority = priority,
    categoryId = categoryId
)

fun TaskModel.toEntity() = TaskEntity(
    id = id,
    userId = userId, // Mapeo del nuevo campo
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt,

    // Convertimos los objetos kotlinx-datetime a primitivos para la BD
    dateMillis = date?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds(),
    timeMillisOfDay = time?.toMillisecondOfDay(),
    priority = priority,
    categoryId = categoryId
)
