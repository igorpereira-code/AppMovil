package ucb.edu.bo.todoApp.task.presentation.state

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
    val saveError: String? = null
)
