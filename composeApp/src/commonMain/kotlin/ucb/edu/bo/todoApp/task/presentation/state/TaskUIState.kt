package ucb.edu.bo.todoApp.task.presentation.state

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ucb.edu.bo.todoApp.task.domain.model.TaskModel

data class TaskUIState(
    val tasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Estado del bottom sheet "Add Task"
    val isAddTaskSheetVisible: Boolean = false,
    val newTaskTitle: String = "",
    val newTaskDescription: String = "",
    val isSaving: Boolean = false,
    val saveError: String? = null,

    // NUEVO: Visibilidad de los modales
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isPriorityPickerVisible: Boolean = false,

    // NUEVO: Datos seleccionados por el usuario
    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
    val selectedPriority: Int? = null
)