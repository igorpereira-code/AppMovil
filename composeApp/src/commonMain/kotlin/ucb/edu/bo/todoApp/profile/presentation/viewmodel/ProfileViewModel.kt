package ucb.edu.bo.todoApp.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.login.domain.repository.AuthRepository // IMPORTAMOS EL REPOSITORIO REAL

data class ProfileUIState(
    val userName: String = "",
    val showNameDialog: Boolean = false,
    val showPasswordDialog: Boolean = false,
    val showAvatarDialog: Boolean = false,
    val selectedAvatar: String = "person",
    val showAboutDialog: Boolean = false,
    val showFaqDialog: Boolean = false,
    val showHelpDialog: Boolean = false,
    val showSupportDialog: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel(
    private val authRepository: AuthRepository // INYECTAMOS EL REPOSITORIO DE TU COMPAÑERO
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUIState())
    val state: StateFlow<ProfileUIState> = _state.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        // Obtenemos el nombre REAL desde Firebase
        val realName = authRepository.getCurrentUserName()
        _state.value = _state.value.copy(userName = realName)
    }

    fun toggleNameDialog(show: Boolean) {
        _state.value = _state.value.copy(showNameDialog = show)
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            authRepository.updateName(newName).onSuccess {
                _state.value = _state.value.copy(userName = newName, showNameDialog = false)
            }.onFailure {
                // Podrías manejar un error visual aquí si lo deseas
                _state.value = _state.value.copy(showNameDialog = false)
            }
        }
    }

    fun togglePasswordDialog(show: Boolean) {
        _state.value = _state.value.copy(showPasswordDialog = show, errorMessage = null)
    }

    fun updatePassword(newPass: String) {
        viewModelScope.launch {
            authRepository.changePassword(newPass).onSuccess {
                _state.value = _state.value.copy(showPasswordDialog = false, errorMessage = null)
            }.onFailure { error ->
                _state.value = _state.value.copy(errorMessage = error.message)
            }
        }
    }

    // Funciones visuales
    fun toggleAvatarDialog(show: Boolean) { _state.value = _state.value.copy(showAvatarDialog = show) }
    fun selectAvatar(avatar: String) { _state.value = _state.value.copy(selectedAvatar = avatar, showAvatarDialog = false) }
    fun toggleAboutDialog(show: Boolean) { _state.value = _state.value.copy(showAboutDialog = show) }
    fun toggleFaqDialog(show: Boolean) { _state.value = _state.value.copy(showFaqDialog = show) }
    fun toggleHelpDialog(show: Boolean) { _state.value = _state.value.copy(showHelpDialog = show) }
    fun toggleSupportDialog(show: Boolean) { _state.value = _state.value.copy(showSupportDialog = show) }
}