package ucb.edu.bo.todoApp.task.data.util

import ucb.edu.bo.todoApp.task.domain.model.TaskModel

/**
 * Utilidad para calcular un hash diferencial de una tarea.
 * Si el hash coincide con el hash remoto, la tarea NO se transfiere.
 * Esto implementa la "compresión diferencial" del ejercicio #4.
 */
object TaskHashUtil {

    /**
     * Genera un hash único basado en los campos relevantes de la tarea.
     * Solo incluye campos que el usuario puede modificar (excluye isSynced, createdAt).
     */
    fun computeHash(task: TaskModel): String {
        val raw = buildString {
            append(task.id)
            append("|")
            append(task.title)
            append("|")
            append(task.description)
            append("|")
            append(task.isCompleted)
            append("|")
            append(task.date?.toString() ?: "null")
            append("|")
            append(task.time?.toString() ?: "null")
            append("|")
            append(task.priority)
            append("|")
            append(task.categoryId ?: "null")
        }
        // Hash simple pero efectivo con la stdlib de Kotlin (sin dependencias externas)
        return raw.hashCode().toString(16)
    }
}
