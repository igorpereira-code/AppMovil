package ucb.edu.bo.todoApp.settings.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.* // Importa todas tus llaves y drawables
import org.jetbrains.compose.resources.stringResource
import ucb.edu.bo.todoApp.settings.presentation.composable.ColorSelectionModal
import ucb.edu.bo.todoApp.settings.presentation.composable.LanguageSelectionModal
import ucb.edu.bo.todoApp.settings.presentation.composable.SettingItem
import ucb.edu.bo.todoApp.settings.presentation.composable.TypographySelectionModal
import ucb.edu.bo.todoApp.settings.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
        ) {
            // ── Top Bar ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Text("<", color = Color.White, fontSize = 24.sp)
                }
                Text(
                    text = stringResource(Res.string.settings_title),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Categoría: Personalización ───────────────────────────────────
            Text(
                text = stringResource(Res.string.settings_title),
                color = Color(0xFF888888),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            SettingItem(
                iconRes = Res.drawable.brush,
                title = stringResource(Res.string.change_color),
                onClick = { viewModel.showColorModal() }
            )

            SettingItem(
                iconRes = Res.drawable.text,
                title = stringResource(Res.string.change_typography),
                onClick = { viewModel.showTypographyModal() }
            )

            SettingItem(
                iconRes = Res.drawable.language_square,
                title = stringResource(Res.string.change_language),
                onClick = { viewModel.showLanguageModal() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Categoría: Importación ───────────────────────────────────────
            /*Text(
                text = stringResource(Res.string.import_title),
                color = Color(0xFF888888),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            SettingItem(
                iconRes = Res.drawable.import,
                title = stringResource(Res.string.import_google),
                onClick = { viewModel.importGoogleCalendar() }
            )

            // ⚠️ CONTROL VISUAL IMPORTANTE: DEBEN ESTAR ADENTRO DEL COLUMN
            // De esta forma respetan el espacio abajo del botón y no se encima nada
            if (state.isImporting) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            state.importError?.let { error ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Error: Módulo en desarrollo (Requiere OAuth2 Real).\nDetalle técnico: $error",
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }*/
        }

        // ── DIBUJO DEL MODAL SUPERPUESTO ─────────────────────────────────────────
        if (state.isLanguageModalVisible) {
            LanguageSelectionModal(
                currentLanguageCode = state.currentLanguage,
                onLanguageSelected = { viewModel.onLanguageSelected(it) },
                onDismiss = { viewModel.hideLanguageModal() }
            )
        }

        // ── DIBUJO DEL MODAL DE COLOR ─────────────────────────────────────────
        if (state.isColorModalVisible) {
            ColorSelectionModal(
                currentColorHex = state.currentAppColorHex,
                onColorSelected = { viewModel.onColorSelected(it) },
                onDismiss = { viewModel.hideColorModal() }
            )
        }
        // ── DIBUJO DEL MODAL DE TIPOGRAFÍA ────────────────────────────────────
        if (state.isTypographyModalVisible) {
            TypographySelectionModal(
                currentTypography = state.currentTypography,
                onTypographySelected = { viewModel.onTypographySelected(it) },
                onDismiss = { viewModel.hideTypographyModal() }
            )
        }

    }
}
