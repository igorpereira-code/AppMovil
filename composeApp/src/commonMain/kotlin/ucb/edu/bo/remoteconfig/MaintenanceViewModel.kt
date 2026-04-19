package ucb.edu.bo.remoteconfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class MaintenanceState {
    data object Loading : MaintenanceState()
    data object UnderMaintenance : MaintenanceState()
    data object Operational : MaintenanceState()
    data class Error(val message: String) : MaintenanceState()
}

class MaintenanceViewModel(
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MaintenanceState>(MaintenanceState.Loading)
    val state: StateFlow<MaintenanceState> = _state.asStateFlow()

    init {
        observeMaintenance()
    }

    private fun observeMaintenance() {
        viewModelScope.launch {
            remoteConfigRepository
                .observeMaintenance()
                .catch { e ->
                    _state.value = MaintenanceState.Error(e.message ?: "Error desconocido")
                }
                .collect { isMaintenance ->
                    _state.value = if (isMaintenance) {
                        MaintenanceState.UnderMaintenance
                    } else {
                        MaintenanceState.Operational
                    }
                }
        }
    }
}

