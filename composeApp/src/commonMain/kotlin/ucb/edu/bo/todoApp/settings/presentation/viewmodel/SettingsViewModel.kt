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
    val currentLanguage: String = "en",
    // ── ESTADO DEL COLOR ──
    val isColorModalVisible: Boolean = false,
    val currentAppColorHex: String = "FF8687E7", // Tu morado por defecto
    val isTypographyModalVisible: Boolean = false,
    val currentTypography: String = "Default"
)

class SettingsViewModel(
    private val settingsPreferences: ISettingsPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUIState())
    val state: StateFlow<SettingsUIState> = _state.asStateFlow()

    init {
        // Leemos el idioma y el color guardados al abrir la app
        viewModelScope.launch {
            settingsPreferences.getLanguage().collect { savedLanguage ->
                _state.value = _state.value.copy(currentLanguage = savedLanguage)
            }
        }
        viewModelScope.launch {
            settingsPreferences.getAppColor().collect { savedColor ->
                _state.value = _state.value.copy(currentAppColorHex = savedColor)
            }
        }
        viewModelScope.launch {
            settingsPreferences.getTypography().collect { savedFont ->
                _state.value = _state.value.copy(currentTypography = savedFont)
            }
        }
    }

    // ── CONTROLES DE IDIOMA ──────────────────────────────────────────────────

    fun showLanguageModal() {
        _state.value = _state.value.copy(isLanguageModalVisible = true)
    }

    fun hideLanguageModal() {
        _state.value = _state.value.copy(isLanguageModalVisible = false)
    }

    // Nota: Aquí le cambiamos el nombre ligeramente al parámetro para no confundir
    // con el de color, pero funciona igual.
    fun onLanguageSelected(languageCode: String) {
        _state.value = _state.value.copy(
            currentLanguage = languageCode,
            isLanguageModalVisible = false
        )
        // Guardamos físicamente la elección
        viewModelScope.launch {
            settingsPreferences.saveLanguage(languageCode)
        }
    }

    // ── CONTROLES DE COLOR ───────────────────────────────────────────────────

    fun showColorModal() {
        _state.value = _state.value.copy(isColorModalVisible = true)
    }

    fun hideColorModal() {
        _state.value = _state.value.copy(isColorModalVisible = false)
    }

    fun onColorSelected(colorHex: String) {
        _state.value = _state.value.copy(
            currentAppColorHex = colorHex,
            isColorModalVisible = false
        )
        // Guardamos el color elegido en el DataStore
        viewModelScope.launch {
            settingsPreferences.saveAppColor(colorHex)
        }
    }
    // ── CONTROLES DE TIPOGRAFÍA ──────────────────────────────────────────────

    fun showTypographyModal() {
        _state.value = _state.value.copy(isTypographyModalVisible = true)
    }

    fun hideTypographyModal() {
        _state.value = _state.value.copy(isTypographyModalVisible = false)
    }

    fun onTypographySelected(fontName: String) {
        _state.value = _state.value.copy(
            currentTypography = fontName,
            isTypographyModalVisible = false
        )
        // Guardamos físicamente
        viewModelScope.launch {
            settingsPreferences.saveTypography(fontName)
        }
    }
}