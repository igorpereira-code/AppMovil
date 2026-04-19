package ucb.edu.bo.realtimedatabasecmp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.realtimedatabasecmp.domain.usecase.SaveTestDataUseCase
import ucb.edu.bo.realtimedatabasecmp.presentation.state.FirebaseTestState

class FirebaseTestViewModel(
    private val saveTestDataUseCase: SaveTestDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FirebaseTestState())
    val state: StateFlow<FirebaseTestState> = _state.asStateFlow()

    fun onInputChange(text: String) {
        _state.value = _state.value.copy(
            inputText = text,
            successMessage = null,
            errorMessage = null
        )
    }

    fun onSaveClick() {
        val text = _state.value.inputText
        if (text.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "El campo no puede estar vacío")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = saveTestDataUseCase(path = "test/valor", value = text)
            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    isLoading = false,
                    successMessage = "✅ Guardado en Firebase correctamente",
                    inputText = ""
                )
            } else {
                _state.value.copy(
                    isLoading = false,
                    errorMessage = "❌ Error: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }
}