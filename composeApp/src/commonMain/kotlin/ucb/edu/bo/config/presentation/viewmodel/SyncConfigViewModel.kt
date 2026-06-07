package ucb.edu.bo.config.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.config.domain.usecase.SyncInitialConfigUseCase
import ucb.edu.bo.config.presentation.state.SyncConfigState

class SyncConfigViewModel(
    private val syncInitialConfigUseCase: SyncInitialConfigUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SyncConfigState())
    val state: StateFlow<SyncConfigState> = _state.asStateFlow()

    init {
        // Ejecutamos la sincronización al arrancar
        syncConfig("welcome_message") // <-- Reemplaza con la clave que tengas en tu Firebase
    }

    private fun syncConfig(key: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val result = syncInitialConfigUseCase(key)

                if (result != null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        configValue = result.value
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No hay datos en caché y no se pudo conectar"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }
}