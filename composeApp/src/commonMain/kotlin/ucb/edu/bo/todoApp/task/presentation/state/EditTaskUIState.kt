package ucb.edu.bo.todoApp.task.presentation.state

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ucb.edu.bo.todoApp.category.domain.model.CategoryModel

data class EditTaskUIState(
    // Estado de carga inicial
    val isLoading: Boolean = false,
    val loadError: String? = null,

    // Campos editables
    val taskId: Int = 0,
    val userId: String = "", // NUEVO: Para no perder el dueño al editar
    val title: String = "",
    val description: String = "",
    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
    val selectedPriority: Int = 1,
    val selectedCategoryId: Int? = null,

    // Categorías disponibles
    val categories: List<CategoryModel> = emptyList(),

    // Visibilidad de modales
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isPriorityPickerVisible: Boolean = false,
    val isCategoryPickerVisible: Boolean = false,

    // Dialog para editar el título
    val isTitleDialogVisible: Boolean = false,
    val titleDialogDraft: String = "",

    // Estado de guardado
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val isSavedSuccessfully: Boolean = false,

    // Estado de eliminación
    val isDeleting: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false
)
