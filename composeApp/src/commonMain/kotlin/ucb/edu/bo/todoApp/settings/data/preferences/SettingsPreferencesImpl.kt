package ucb.edu.bo.todoApp.settings.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ucb.edu.bo.todoApp.settings.domain.preferences.ISettingsPreferences

class SettingsPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : ISettingsPreferences {

    // Definimos las "llaves" exactas con las que se guardará cada valor
    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
        val COLOR_KEY = stringPreferencesKey("app_color")
        val TYPOGRAPHY_KEY = stringPreferencesKey("app_typography")
    }

    // ── IDIOMA ───────────────────────────────────────────────────────────────
    override suspend fun saveLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }

    override fun getLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            // "en" será el idioma por defecto si es la primera vez que abre la app
            preferences[LANGUAGE_KEY] ?: "en"
        }
    }

    // ── COLOR (Listos para después) ──────────────────────────────────────────
    override suspend fun saveAppColor(colorHex: String) {
        dataStore.edit { preferences ->
            preferences[COLOR_KEY] = colorHex
        }
    }

    override fun getAppColor(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[COLOR_KEY] ?: "#8687E7" // Tu PrimaryPurple por defecto
        }
    }

    // ── TIPOGRAFÍA (Listos para después) ─────────────────────────────────────
    override suspend fun saveTypography(fontName: String) {
        dataStore.edit { preferences ->
            preferences[TYPOGRAPHY_KEY] = fontName
        }
    }

    override fun getTypography(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TYPOGRAPHY_KEY] ?: "Default"
        }
    }
}