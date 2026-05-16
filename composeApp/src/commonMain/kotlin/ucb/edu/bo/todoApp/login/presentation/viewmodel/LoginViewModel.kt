package ucb.edu.bo.todoApp.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.login.domain.usecase.LoginUseCase
import ucb.edu.bo.todoApp.login.presentation.state.LoginState

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Init)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        _state.value = LoginState.Loading
        viewModelScope.launch {
            loginUseCase(email, password)
                .onSuccess { _state.value = LoginState.Success }
                .onFailure { _state.value = LoginState.Error(it.message ?: "Error desconocido") }
        }
    }

    fun resetState() {
        _state.value = LoginState.Init
    }
}