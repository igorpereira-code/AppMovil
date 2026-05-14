package ucb.edu.bo.app.home.presentation.state

import ucb.edu.bo.app.home.domain.model.Task

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val showAddTaskSheet: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val showPriorityPicker: Boolean = false,
    val newTaskTitle: String = "",
    val newTaskDescription: String = "",
    val selectedDate: String = "",
    val selectedTime: String = "",
    val selectedPriority: Int = 0
)
