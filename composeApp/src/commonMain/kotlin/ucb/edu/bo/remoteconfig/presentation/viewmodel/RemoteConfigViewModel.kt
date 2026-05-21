package ucb.edu.bo.remoteconfig.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.remoteconfig.domain.usecase.GetCachedConfigUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.SyncRemoteConfigUseCase
import ucb.edu.bo.remoteconfig.presentation.state.RemoteConfigState

class RemoteConfigViewModel(
    private val syncRemoteConfigUseCase: SyncRemoteConfigUseCase,
    private val getCachedConfigUseCase: GetCachedConfigUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RemoteConfigState())
    val state: StateFlow<RemoteConfigState> = _state.asStateFlow()

    init {
        loadConfig()
    }

    private fun loadConfig() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            // Primero intentar obtener de Remote Config
            val remoteResult = syncRemoteConfigUseCase("welcome_message")
            if (remoteResult.isSuccess) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    value = remoteResult.getOrNull() ?: "",
                    isFromCache = false
                )
            } else {
                // Si falla, usar caché de Room
                val cached = getCachedConfigUseCase("welcome_message")
                if (cached != null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        value = cached.value,
                        isFromCache = true
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No hay datos disponibles"
                    )
                }
            }
        }
    }
}