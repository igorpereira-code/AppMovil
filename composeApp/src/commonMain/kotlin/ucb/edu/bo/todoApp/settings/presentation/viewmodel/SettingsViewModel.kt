package ucb.edu.bo.todoApp.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.settings.domain.preferences.ISettingsPreferences

data class SettingsUIState(
    val isLanguageModalVisible: Boolean = false,
    val currentLanguage: String = "en"
)

class SettingsViewModel(
    private val settingsPreferences: ISettingsPreferences // NUEVO: Inyectamos el DataStore
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUIState())
    val state: StateFlow<SettingsUIState> = _state.asStateFlow()

    init {
        // Al abrir la pantalla de Settings, leemos qué idioma estaba guardado
        viewModelScope.launch {
            settingsPreferences.getLanguage().collect { savedLanguage ->
                _state.value = _state.value.copy(currentLanguage = savedLanguage)
            }
        }
    }

    fun showLanguageModal() {
        _state.value = _state.value.copy(isLanguageModalVisible = true)
    }

    fun hideLanguageModal() {
        _state.value = _state.value.copy(isLanguageModalVisible = false)
    }

    fun onLanguageSelected(languageCode: String) {
        _state.value = _state.value.copy(
            currentLanguage = languageCode,
            isLanguageModalVisible = false
        )
        // Guardamos físicamente la elección en el teléfono
        viewModelScope.launch {
            settingsPreferences.saveLanguage(languageCode)
        }
    }
}