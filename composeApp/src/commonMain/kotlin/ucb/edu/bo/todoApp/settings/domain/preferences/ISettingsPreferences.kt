package ucb.edu.bo.todoApp.settings.domain.preferences

import kotlinx.coroutines.flow.Flow

interface ISettingsPreferences {
    // Para el idioma
    suspend fun saveLanguage(languageCode: String)
    fun getLanguage(): Flow<String>

    // Los dejamos listos para cuando hagamos el color y tipografía
    suspend fun saveAppColor(colorHex: String)
    fun getAppColor(): Flow<String>

    suspend fun saveTypography(fontName: String)
    fun getTypography(): Flow<String>

    suspend fun saveThemeMode(isDark: Boolean)

    fun getThemeMode(): Flow<Boolean>
}