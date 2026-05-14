package ucb.edu.bo.app.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ucb.edu.bo.app.home.domain.model.Task
import ucb.edu.bo.app.home.domain.usecase.GetTasksUseCase
import ucb.edu.bo.app.home.domain.usecase.SaveTaskUseCase
import ucb.edu.bo.app.home.presentation.state.HomeUiState

class HomeViewModel(
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        getTasksUseCase()
            .onEach { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }
            .launchIn(viewModelScope)
    }

    fun onAddTaskClicked() {
        _uiState.update { it.copy(showAddTaskSheet = true) }
    }

    fun onDismissAddTask() {
        _uiState.update { it.copy(showAddTaskSheet = false) }
    }

    fun onTaskTitleChanged(title: String) {
        _uiState.update { it.copy(newTaskTitle = title) }
    }

    fun onTaskDescriptionChanged(description: String) {
        _uiState.update { it.copy(newTaskDescription = description) }
    }

    fun onTimerClicked() {
        _uiState.update { it.copy(showDatePicker = true) }
    }

    fun onDateSelected(date: String) {
        _uiState.update { it.copy(selectedDate = date, showDatePicker = false, showTimePicker = true) }
    }

    fun onTimeSelected(time: String) {
        _uiState.update { it.copy(selectedTime = time, showTimePicker = false) }
    }

    fun onPriorityClicked() {
        _uiState.update { it.copy(showPriorityPicker = true) }
    }

    fun onPrioritySelected(priority: Int) {
        _uiState.update { it.copy(selectedPriority = priority) }
    }

    fun onSavePriority() {
        _uiState.update { it.copy(showPriorityPicker = false) }
    }

    fun onDismissDatePicker() {
        _uiState.update { it.copy(showDatePicker = false) }
    }

    fun onDismissTimePicker() {
        _uiState.update { it.copy(showTimePicker = false) }
    }

    fun onDismissPriorityPicker() {
        _uiState.update { it.copy(showPriorityPicker = false) }
    }

    fun onSaveTask() {
        val currentState = _uiState.value
        if (currentState.newTaskTitle.isNotBlank()) {
            val dateTime = if (currentState.selectedDate.isNotEmpty() && currentState.selectedTime.isNotEmpty()) {
                "${currentState.selectedDate} at ${currentState.selectedTime}"
            } else ""
            
            val newTask = Task(
                title = currentState.newTaskTitle,
                description = currentState.newTaskDescription,
                time = dateTime,
                priority = currentState.selectedPriority
            )
            
            viewModelScope.launch {
                saveTaskUseCase(newTask)
                _uiState.update { 
                    it.copy(
                        showAddTaskSheet = false,
                        newTaskTitle = "",
                        newTaskDescription = "",
                        selectedDate = "",
                        selectedTime = "",
                        selectedPriority = 0
                    )
                }
            }
        }
    }
}
