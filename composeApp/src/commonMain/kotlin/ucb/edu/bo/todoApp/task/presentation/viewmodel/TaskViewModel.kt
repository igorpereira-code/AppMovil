package ucb.edu.bo.todoApp.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import ucb.edu.bo.todoApp.task.domain.usecase.CreateTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.DeleteTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.GetAllTasksUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.ToggleTaskUseCase
import ucb.edu.bo.todoApp.task.presentation.state.TaskUIState

class TaskViewModel(
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

        // Capturamos los datos seleccionados de los modales
        val date = _state.value.selectedDate
        val time = _state.value.selectedTime
        val priority = _state.value.selectedPriority ?: 1 // 1 por defecto

        _state.value = _state.value.copy(isSaving = true)
        viewModelScope.launch {
            val newTask = TaskModel(
                title = title,
                description = description,
                date = date,
                time = time,
                priority = priority
            )

            createTaskUseCase(newTask)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        isAddTaskSheetVisible = false,
                        newTaskTitle = "",
                        newTaskDescription = "",
                        // Limpiamos los campos para la próxima tarea
                        selectedDate = null,
                        selectedTime = null,
                        selectedPriority = null
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
    // ── Modales de Fecha, Hora y Prioridad ──────────────────────────────────────

    fun showDatePicker() {
        _state.value = _state.value.copy(isDatePickerVisible = true)
    }
    fun hideDatePicker() {
        _state.value = _state.value.copy(isDatePickerVisible = false)
    }
    fun onDateSelected(date: LocalDate) {
        _state.value = _state.value.copy(
            selectedDate = date,
            isDatePickerVisible = false, // Cerramos el modal de fecha
            isTimePickerVisible = true   // Abrimos automáticamente el modal de hora
        )
    }

    // Formateador de tiempo KMP
    fun formatTaskTimeText(taskDate: LocalDate?, taskTime: LocalTime?): String {
        if (taskDate == null || taskTime == null) return "Sin programar"

        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date

        val tomorrow = today.plus(1, DateTimeUnit.DAY)

        val dateText = when (taskDate) {
            today -> "Today"
            tomorrow -> "Tomorrow"
            else -> {
                val monthName = taskDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
                "$monthName ${taskDate.dayOfMonth}"
            }
        }

        val hourText = taskTime.hour.toString().padStart(2, '0')
        val minuteText = taskTime.minute.toString().padStart(2, '0')

        return "$dateText At $hourText:$minuteText"
    }

    fun showTimePicker() {
        _state.value = _state.value.copy(isTimePickerVisible = true)
    }
    fun hideTimePicker() {
        _state.value = _state.value.copy(isTimePickerVisible = false)
    }
    fun onTimeSelected(time: LocalTime) {
        _state.value = _state.value.copy(selectedTime = time, isTimePickerVisible = false)
    }

    fun showPriorityPicker() {
        _state.value = _state.value.copy(isPriorityPickerVisible = true)
    }
    fun hidePriorityPicker() {
        _state.value = _state.value.copy(isPriorityPickerVisible = false)
    }
    fun onPrioritySelected(priority: Int) {
        _state.value = _state.value.copy(selectedPriority = priority, isPriorityPickerVisible = false)
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