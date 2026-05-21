package ucb.edu.bo.todoApp.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import ucb.edu.bo.todoApp.task.domain.usecase.CreateTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.DeleteTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.ToggleTaskUseCase
import ucb.edu.bo.todoApp.task.presentation.state.TaskUIState

class TaskViewModel (
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val toggleTaskUseCase: ToggleTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskUIState())
    val state: StateFlow<TaskUIState> = _state.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val tasks = getAllTasksUseCase()
            _state.value = _state.value.copy(
                tasks = tasks,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    // ── Add Task Sheet ──────────────────────────────────────────────────────────

    fun showAddTaskSheet() {
        _state.value = _state.value.copy(
            isAddTaskSheetVisible = true,
            newTaskTitle = "",
            newTaskDescription = "",
            saveError = null
        )
    }

    fun hideAddTaskSheet() {
        _state.value = _state.value.copy(isAddTaskSheetVisible = false)
    }

    fun onTitleChange(title: String) {
        _state.value = _state.value.copy(newTaskTitle = title, saveError = null)
    }

    fun onDescriptionChange(description: String) {
        _state.value = _state.value.copy(newTaskDescription = description, saveError = null)
    }

    fun saveTask() {
        val title = _state.value.newTaskTitle
        val description = _state.value.newTaskDescription

        _state.value = _state.value.copy(isSaving = true)
        viewModelScope.launch {
            createTaskUseCase(TaskModel(title = title, description = description))
                .onSuccess {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        isAddTaskSheetVisible = false,
                        newTaskTitle = "",
                        newTaskDescription = ""
                    )
                    loadTasks()
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        saveError = it.message
                    )
                }
        }
    }

    // ── Task actions ────────────────────────────────────────────────────────────

    fun toggleTask(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            toggleTaskUseCase(taskId, isCompleted)
            loadTasks()
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
            loadTasks()
        }
    }
}