package ucb.edu.bo.todoApp.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.settings.domain.preferences.ISettingsPreferences
import ucb.edu.bo.todoApp.settings.domain.repository.QuoteRepository
import ucb.edu.bo.todoApp.settings.domain.usecase.ImportGoogleCalendarUseCase

data class SettingsUIState(
    val isLanguageModalVisible: Boolean = false,
    val currentLanguage: String = "en",
    // ── ESTADO DEL COLOR ──
    val isColorModalVisible: Boolean = false,
    val currentAppColorHex: String = "FF8687E7", // Tu morado por defecto
    val isTypographyModalVisible: Boolean = false,
    val currentTypography: String = "Default",
    val isImporting: Boolean = false,
    val importError: String? = null,
    val isThemeModeModalVisible: Boolean = false,
    val isDarkMode: Boolean = true,
    val dailyQuote: String = "Cargando frase..."
)

class SettingsViewModel(
    private val settingsPreferences: ISettingsPreferences,
    private val importGoogleCalendarUseCase: ImportGoogleCalendarUseCase, // Inyectamos el Caso de Uso
    private val quoteRepository: QuoteRepository
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
        viewModelScope.launch {
            settingsPreferences.getThemeMode().collect { isDark ->
                _state.value = _state.value.copy(isDarkMode = isDark)
            }
        }
        viewModelScope.launch {
            val quote = quoteRepository.getRandomMotivationalQuote()
            _state.value = _state.value.copy(dailyQuote = quote)
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

    // ── NUEVO: LOGICA DE IMPORTACIÓN DESDE GOOGLE CALENDAR ───────────────────
    fun importGoogleCalendar() {
        _state.value = _state.value.copy(isImporting = true, importError = null)

        viewModelScope.launch {
            val result = importGoogleCalendarUseCase()
            result.onSuccess {
                _state.value = _state.value.copy(isImporting = false)
                // Opcional: Podrías gatillar una bandera de éxito para mostrar un Toast o diálogo
            }.onFailure { error ->
                _state.value = _state.value.copy(
                    isImporting = false,
                    importError = error.message ?: "Unknown error during import"
                )
            }
        }
    }
    fun showThemeModeModal() {
        _state.value = _state.value.copy(isThemeModeModalVisible = true)
    }

    fun hideThemeModeModal() {
        _state.value = _state.value.copy(isThemeModeModalVisible = false)
    }

    fun onThemeModeSelected(isDark: Boolean) {
        _state.value = _state.value.copy(
            isDarkMode = isDark,
            isThemeModeModalVisible = false
        )
        viewModelScope.launch {
            settingsPreferences.saveThemeMode(isDark)
        }
    }
}