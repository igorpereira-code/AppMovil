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
import ucb.edu.bo.todoApp.category.domain.usecase.GetAllCategoriesUseCase

class TaskViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val toggleTaskUseCase: ToggleTaskUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
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
            val categories = getAllCategoriesUseCase() // Carga las categorías
            _state.value = _state.value.copy(
                tasks = tasks,
                categories = categories, // Las guarda en el estado
                isLoading = false,
                errorMessage = null
            )
        }
    }

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
        val date = _state.value.selectedDate
        val time = _state.value.selectedTime
        val priority = _state.value.selectedPriority ?: 1
        val categoryId = _state.value.selectedCategoryId

        _state.value = _state.value.copy(isSaving = true)
        viewModelScope.launch {
            val newTask = TaskModel(
                title = title,
                description = description,
                date = date,
                time = time,
                priority = priority,
                categoryId = categoryId
            )

            createTaskUseCase(newTask)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        isAddTaskSheetVisible = false,
                        newTaskTitle = "",
                        newTaskDescription = "",
                        selectedDate = null,
                        selectedTime = null,
                        selectedPriority = null,
                        selectedCategoryId = null
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

    fun showDatePicker() {
        _state.value = _state.value.copy(isDatePickerVisible = true)
    }

    fun hideDatePicker() {
        _state.value = _state.value.copy(isDatePickerVisible = false)
    }

    fun onDateSelected(date: LocalDate) {
        _state.value = _state.value.copy(
            selectedDate = date,
            isDatePickerVisible = false,
            isTimePickerVisible = true
        )
    }

    fun formatTaskTimeText(taskDate: LocalDate?, taskTime: LocalTime?): String? {
        if (taskDate == null || taskTime == null) return null

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

    fun showCategoryPicker() {
        _state.value = _state.value.copy(isCategoryPickerVisible = true)
    }

    fun hideCategoryPicker() {
        _state.value = _state.value.copy(isCategoryPickerVisible = false)
    }

    fun onCategorySelected(categoryId: Int) {
        _state.value = _state.value.copy(
            selectedCategoryId = categoryId,
            isCategoryPickerVisible = false
        )
    }

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