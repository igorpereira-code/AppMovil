package ucb.edu.bo.utils

import java.util.Locale

// Implementación nativa para Android
actual fun setAppLanguage(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    // Si necesitas soportar versiones muy antiguas de Android, aquí también
    // se actualizaría el 'resources.configuration', pero con el truco de Compose
    // que haremos a continuación, el setDefault suele ser suficiente.
}