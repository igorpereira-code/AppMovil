package ucb.edu.bo.todoApp.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.edit_task_error_empty_title
import appmovil.composeapp.generated.resources.edit_task_error_not_found
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.getString
import ucb.edu.bo.todoApp.category.domain.usecase.GetAllCategoriesUseCase
import ucb.edu.bo.todoApp.login.domain.repository.AuthRepository
import ucb.edu.bo.todoApp.task.domain.usecase.DeleteTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.GetTaskByIdUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.UpdateTaskUseCase
import ucb.edu.bo.todoApp.task.presentation.state.EditTaskUIState

class EditTaskViewModel(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val authRepository: AuthRepository // Añadido para mantener la integridad del usuario
) : ViewModel() {

    private val _state = MutableStateFlow(EditTaskUIState())
    val state: StateFlow<EditTaskUIState> = _state.asStateFlow()

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, loadError = null)
            try {
                val task = getTaskByIdUseCase(taskId)
                val categories = getAllCategoriesUseCase()
                if (task != null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        taskId = task.id,
                        userId = task.userId, // Recuperamos el userId original
                        title = task.title,
                        description = task.description,
                        selectedDate = task.date,
                        selectedTime = task.time,
                        selectedPriority = task.priority,
                        selectedCategoryId = task.categoryId,
                        categories = categories
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        loadError = getString(Res.string.edit_task_error_not_found)
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    loadError = e.message
                )
            }
        }
    }

    fun showTitleDialog() {
        _state.value = _state.value.copy(
            isTitleDialogVisible = true,
            titleDialogDraft = _state.value.title
        )
    }

    fun hideTitleDialog() {
        _state.value = _state.value.copy(isTitleDialogVisible = false)
    }

    fun onTitleDialogDraftChange(draft: String) {
        _state.value = _state.value.copy(titleDialogDraft = draft)
    }

    fun confirmTitleEdit() {
        _state.value = _state.value.copy(
            title = _state.value.titleDialogDraft.trim(),
            isTitleDialogVisible = false
        )
    }

    fun onDescriptionChange(desc: String) {
        _state.value = _state.value.copy(description = desc)
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

    fun showTimePicker() {
        _state.value = _state.value.copy(isTimePickerVisible = true)
    }

    fun hideTimePicker() {
        _state.value = _state.value.copy(isTimePickerVisible = false)
    }

    fun onTimeSelected(time: LocalTime) {
        _state.value = _state.value.copy(
            selectedTime = time,
            isTimePickerVisible = false
        )
    }

    fun showPriorityPicker() {
        _state.value = _state.value.copy(isPriorityPickerVisible = true)
    }

    fun hidePriorityPicker() {
        _state.value = _state.value.copy(isPriorityPickerVisible = false)
    }

    fun onPrioritySelected(priority: Int) {
        _state.value = _state.value.copy(
            selectedPriority = priority,
            isPriorityPickerVisible = false
        )
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

    fun saveTask() {
        viewModelScope.launch {
            val s = _state.value
            if (s.title.isBlank()) {
                _state.value = s.copy(
                    saveError = getString(Res.string.edit_task_error_empty_title)
                )
                return@launch
            }

            _state.value = _state.value.copy(isSaving = true, saveError = null)

            val updatedTask = ucb.edu.bo.todoApp.task.domain.model.TaskModel(
                id = s.taskId,
                userId = s.userId, // Mantenemos el mismo userId
                title = s.title,
                description = s.description,
                date = s.selectedDate,
                time = s.selectedTime,
                priority = s.selectedPriority,
                categoryId = s.selectedCategoryId
            )

            updateTaskUseCase(updatedTask)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        isSavedSuccessfully = true
                    )
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        isSaving = false,
                        saveError = e.message
                    )
                }
        }
    }

    fun showDeleteConfirmDialog() {
        _state.value = _state.value.copy(showDeleteConfirmDialog = true)
    }

    fun hideDeleteConfirmDialog() {
        _state.value = _state.value.copy(showDeleteConfirmDialog = false)
    }

    fun deleteTask() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, showDeleteConfirmDialog = false)
            deleteTaskUseCase(_state.value.taskId)
            _state.value = _state.value.copy(
                isDeleting = false,
                isSavedSuccessfully = true
            )
        }
    }
}
