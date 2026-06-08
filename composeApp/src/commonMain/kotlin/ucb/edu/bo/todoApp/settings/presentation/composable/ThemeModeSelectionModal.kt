package ucb.edu.bo.todoApp.settings.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeModeSelectionModal(
    isDarkMode: Boolean,
    onModeSelected: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        // Usamos el color dinámico del tema para el fondo del modal
        containerColor = MaterialTheme.colorScheme.surface,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp, top = 8.dp)
        ) {
            Text(
                text = stringResource(Res.string.settings_title_select_theme),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            // Opción Modo Claro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onModeSelected(false) }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(Res.string.settings_theme_light), color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp)
                if (!isDarkMode) Text("✓", color = MaterialTheme.colorScheme.primary)
            }

            // Opción Modo Oscuro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onModeSelected(true) }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(Res.string.settings_theme_dark), color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp)
                if (isDarkMode) Text("✓", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}