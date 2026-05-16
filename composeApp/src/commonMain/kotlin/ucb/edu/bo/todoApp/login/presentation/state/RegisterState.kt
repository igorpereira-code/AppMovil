package ucb.edu.bo.todoApp.login.presentation.state

sealed class RegisterState {
    object Init : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}