package ucb.edu.bo.todoApp.intro.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import ucb.edu.bo.todoApp.intro.data.model.OnboardingConfigResponse
import ucb.edu.bo.todoApp.intro.domain.model.IntroPage
import ucb.edu.bo.todoApp.intro.domain.repository.IntroRepository

val Context.dataStore by preferencesDataStore(name = "onboarding_prefs")

class IntroRepositoryImpl(
    private val context: Context,
    private val remoteConfig: FirebaseRemoteConfig
) : IntroRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val ONBOARDING_DONE = booleanPreferencesKey("onboarding_completed")

    override suspend fun getIntroPages(lang: String): List<IntroPage> {
        return try {
            // Fetch + activate Remote Config
            remoteConfig.fetchAndActivate().await()

            val raw = remoteConfig.getString("onboarding_config")
            // Remote Config devuelve el array directamente, lo envolvemos
            val wrapped = if (raw.trimStart().startsWith("[")) {
                """{"onboarding_config":$raw}"""
            } else raw

            val response = json.decodeFromString<OnboardingConfigResponse>(wrapped)

            response.onboardingConfig.map { page ->
                IntroPage(
                    id = page.id,
                    title = page.title.forLocale(lang),
                    description = page.description.forLocale(lang),
                    imageUrl = page.imageUrl.forLocale(lang)
                )
            }
        } catch (e: Exception) {
            // Fallback hardcodeado mínimo si Remote Config falla
            emptyList()
        }
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return context.dataStore.data.first()[ONBOARDING_DONE] ?: false
    }

    override suspend fun markOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_DONE] = true
        }
    }
}