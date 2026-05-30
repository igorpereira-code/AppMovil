package ucb.edu.bo.todoApp.task.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class TaskModel(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),

    // NUEVOS CAMPOS: Usando kotlinx-datetime para la lógica pura
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val priority: Int = 1, // 1 por defecto
    val categoryId: Int? = null
)