package ucb.edu.bo.pushnotification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.firebase.getToken
import ucb.edu.bo.pushnotification.presentation.state.PushNotificationState

class PushNotificationViewModel : ViewModel() {

    private val _state = MutableStateFlow(PushNotificationState())
    val state: StateFlow<PushNotificationState> = _state.asStateFlow()

    init {
        fetchToken()
    }

    private fun fetchToken() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val token = getToken()
                _state.value = _state.value.copy(
                    isLoading = false,
                    token = token
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