package ucb.edu.bo.todoApp.login.presentation.state

sealed class LoginState {
    object Init : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}