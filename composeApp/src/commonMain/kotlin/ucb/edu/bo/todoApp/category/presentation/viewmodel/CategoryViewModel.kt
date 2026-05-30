package ucb.edu.bo.todoApp.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.category.domain.model.CategoryModel
import ucb.edu.bo.todoApp.category.domain.usecase.CreateCategoryUseCase
import ucb.edu.bo.todoApp.category.domain.usecase.GetAllCategoriesUseCase
import ucb.edu.bo.todoApp.category.presentation.state.CategoryUIState

class CategoryViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryUIState())
    val state: StateFlow<CategoryUIState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val categories = getAllCategoriesUseCase()
            _state.value = _state.value.copy(
                categories = categories,
                isLoading = false
            )
        }
    }

    fun toggleCreateMode(isCreating: Boolean) {
        _state.value = _state.value.copy(
            isCreatingNew = isCreating,
            saveError = null,
            // Nos aseguramos de que al abrir la pantalla, inicie vacío
            selectedIcon = ""
        )
    }

    fun updateName(name: String) {
        _state.value = _state.value.copy(newCategoryName = name, saveError = null)
    }

    fun selectColor(colorHex: Long) {
        _state.value = _state.value.copy(selectedColor = colorHex)
    }

    fun selectIcon(iconName: String) {
        _state.value = _state.value.copy(selectedIcon = iconName)
    }

    fun saveCategory() {
        val name = _state.value.newCategoryName
        val color = _state.value.selectedColor
        val icon = _state.value.selectedIcon

        viewModelScope.launch {
            createCategoryUseCase(CategoryModel(name = name, colorHex = color, iconResName = icon))
                .onSuccess {
                    _state.value = _state.value.copy(
                        isCreatingNew = false,
                        newCategoryName = "",
                        selectedColor = 0xFFCCFF90,
                        selectedIcon = "" // <-- Reseteamos a vacío para que vuelva a salir el texto
                    )
                    loadCategories()
                }
                .onFailure {
                    _state.value = _state.value.copy(saveError = it.message)
                }
        }
    }
}