package ucb.edu.bo.remoteconfig.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.remoteconfig.domain.usecase.FetchRemoteConfigUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.GetRemoteStringUseCase
import ucb.edu.bo.remoteconfig.presentation.state.RemoteConfigState

class RemoteConfigViewModel(
    private val fetchRemoteConfigUseCase: FetchRemoteConfigUseCase,
    private val getRemoteStringUseCase: GetRemoteStringUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RemoteConfigState())
    val state: StateFlow<RemoteConfigState> = _state.asStateFlow()

    init {
        fetchConfig()
    }

    private fun fetchConfig() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                fetchRemoteConfigUseCase()
                val message = getRemoteStringUseCase("welcome_message")
                _state.value = _state.value.copy(
                    isLoading = false,
                    welcomeMessage = message
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }
}