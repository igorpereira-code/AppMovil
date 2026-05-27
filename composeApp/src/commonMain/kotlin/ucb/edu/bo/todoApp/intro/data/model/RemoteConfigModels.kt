package ucb.edu.bo.todoApp.intro.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingConfigResponse(
    @SerialName("onboarding_config")
    val onboardingConfig: List<RemoteIntroPage>
)

@Serializable
data class RemoteIntroPage(
    val id: Int,
    val title: LocalizedText,
    val description: LocalizedText,
    @SerialName("image_url")
    val imageUrl: LocalizedText
)

@Serializable
data class LocalizedText(
    val es: String = "",
    val en: String = "",
    val fr: String = ""
) {
    fun forLocale(lang: String): String = when (lang) {
        "en" -> en.ifBlank { es }
        "fr" -> fr.ifBlank { es }
        else -> es
    }
}