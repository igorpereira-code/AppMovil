package ucb.edu.bo.todoApp.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.login.domain.usecase.RegisterUseCase
import ucb.edu.bo.todoApp.login.presentation.state.RegisterState

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Init)
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun register(username: String, email: String, password: String) {
        _state.value = RegisterState.Loading
        viewModelScope.launch {
            registerUseCase(username, email, password)
                .onSuccess { _state.value = RegisterState.Success }
                .onFailure { _state.value = RegisterState.Error(it.message ?: "Error desconocido") }
        }
    }

    fun resetState() {
        _state.value = RegisterState.Init
    }
}