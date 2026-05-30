package com.example.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Typography

// Función de extensión segura para convertir Hex a Color
fun String.toColor(): Color {
    return try {
        Color(this.toLong(16))
    } catch (e: Exception) {
        Color(0xFF8687E7) // Color por defecto (Morado) si algo sale mal
    }
}

fun getFontFamily(fontName: String): FontFamily {
    return when (fontName) {
        "Serif" -> FontFamily.Serif
        "Monospace" -> FontFamily.Monospace
        "SansSerif" -> FontFamily.SansSerif
        else -> FontFamily.Default
    }
}

@Composable
fun AppTheme(
    dynamicPrimaryColorHex: String,
    dynamicTypography: String = "Default",
    content: @Composable () -> Unit
) {
    // 1. Convertimos el Hex guardado en el teléfono a un objeto Color
    val primaryColor = dynamicPrimaryColorHex.toColor()
    val fontFamily = getFontFamily(dynamicTypography)

    // 2. Definimos la paleta de colores.
    // Como tu diseño es oscuro, usaremos darkColorScheme.
    val darkColors = darkColorScheme(
        primary = primaryColor,               // <--- ¡AQUÍ ESTÁ LA MAGIA DEL COLOR DINÁMICO!
        background = Color(0xFF121212),       // El fondo global oscuro de tus pantallas
        surface = Color(0xFF363636),          // El fondo de tus BottomSheets y Tarjetas
        onPrimary = Color.White,              // El color del texto sobre botones primarios
        onBackground = Color.White,           // El color del texto principal
        surfaceVariant = Color(0xFF1D1D1D)    // Color secundario oscuro para chips o menús
    )
    val defaultTypography = Typography()
    val appTypography = Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily)
    )

    // 3. Inyectamos esta paleta a toda la aplicación
    MaterialTheme(
        colorScheme = darkColors,
        typography = appTypography,
        content = content
    )
}