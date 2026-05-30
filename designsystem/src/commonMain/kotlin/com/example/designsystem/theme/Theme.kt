package com.example.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Función de extensión segura para convertir Hex a Color
fun String.toColor(): Color {
    return try {
        Color(this.toLong(16))
    } catch (e: Exception) {
        Color(0xFF8687E7) // Color por defecto (Morado) si algo sale mal
    }
}

@Composable
fun AppTheme(
    dynamicPrimaryColorHex: String,
    content: @Composable () -> Unit
) {
    // 1. Convertimos el Hex guardado en el teléfono a un objeto Color
    val primaryColor = dynamicPrimaryColorHex.toColor()

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

    // 3. Inyectamos esta paleta a toda la aplicación
    MaterialTheme(
        colorScheme = darkColors,
        content = content
    )
}