package ucb.edu.bo.todoApp.settings.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import appmovil.composeapp.generated.resources.* 
import org.jetbrains.compose.resources.stringResource
import ucb.edu.bo.todoApp.focus_mode.presentation.viewmodel.FocusViewModel
import ucb.edu.bo.todoApp.settings.presentation.composable.ColorSelectionModal
import ucb.edu.bo.todoApp.settings.presentation.composable.LanguageSelectionModal
import ucb.edu.bo.todoApp.settings.presentation.composable.SettingItem
import ucb.edu.bo.todoApp.settings.presentation.composable.ThemeModeSelectionModal
import ucb.edu.bo.todoApp.settings.presentation.composable.TypographySelectionModal
import ucb.edu.bo.todoApp.settings.presentation.viewmodel.SettingsViewModel
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = koinViewModel(),
    onLogout: () -> Unit
)
{
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // ── Top Bar ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Text("<", color = MaterialTheme.colorScheme.onBackground, fontSize = 24.sp)
                }
                Text(
                    text = stringResource(Res.string.settings_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Categoría: Personalización ───────────────────────────────────
            Text(
                text = stringResource(Res.string.settings_title),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
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

            SettingItem(
                iconRes = Res.drawable.moon,
                title = stringResource(Res.string.settings_item_display_mode),
                onClick = { viewModel.showThemeModeModal() }
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = onLogout) {
                Text(
                    text = stringResource(Res.string.focus_button_logout),
                    color = Color(0xFFE74C3C),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Frase motivacional
            Text(
                text = state.dailyQuote,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier.padding(24.dp).align(Alignment.CenterHorizontally),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── DIBUJO DEL MODAL SUPERPUESTO ─────────────────────────────────────────
        if (state.isLanguageModalVisible) {
            LanguageSelectionModal(
                currentLanguageCode = state.currentLanguage,
                onLanguageSelected = { viewModel.onLanguageSelected(it) },
                onDismiss = { viewModel.hideLanguageModal() }
            )
        }

        if (state.isColorModalVisible) {
            ColorSelectionModal(
                currentColorHex = state.currentAppColorHex,
                onColorSelected = { viewModel.onColorSelected(it) },
                onDismiss = { viewModel.hideColorModal() }
            )
        }
        if (state.isTypographyModalVisible) {
            TypographySelectionModal(
                currentTypography = state.currentTypography,
                onTypographySelected = { viewModel.onTypographySelected(it) },
                onDismiss = { viewModel.hideTypographyModal() }
            )
        }
        if (state.isThemeModeModalVisible) {
            ThemeModeSelectionModal(
                isDarkMode = state.isDarkMode,
                onModeSelected = { viewModel.onThemeModeSelected(it) },
                onDismiss = { viewModel.hideThemeModeModal() }
            )
        }

    }
}
