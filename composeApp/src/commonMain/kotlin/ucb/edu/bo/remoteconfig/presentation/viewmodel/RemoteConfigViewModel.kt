package ucb.edu.bo.remoteconfig.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.dollar.domain.usecase.GetDollarListUseCase
import ucb.edu.bo.remoteconfig.presentation.state.RemoteConfigState

class RemoteConfigViewModel(
    private val getDollarListUseCase: GetDollarListUseCase // Solo necesitamos leer de Room
) : ViewModel() {

    private val _state = MutableStateFlow(RemoteConfigState())
    val state: StateFlow<RemoteConfigState> = _state.asStateFlow()

    init {
        loadFromCache()
    }

    fun loadFromCache() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                // 1. Consultamos Room a través del UseCase de Dollar
                val cache = getDollarListUseCase.invoke()

                // 2. Buscamos el registro que guardó el InitialSyncWorker
                val lastValue = cache.lastOrNull { it.dollarParallel == "FROM_REMOTE" }

                _state.value = _state.value.copy(
                    isLoading = false,
                    welcomeMessage = lastValue?.dollarOfficial ?: "Esperando sincronización inicial..."
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error al leer caché: ${e.message}"
                )
            }
        }
    }
}