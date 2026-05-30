package ucb.edu.bo.todoApp.settings.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.select_language // Llave de Localise.biz
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionModal(
    currentLanguageCode: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val supportedLanguages = listOf(
        Pair("en", "English"),
        Pair("es", "Español")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF363636),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, top = 8.dp)
        ) {
            Text(
                text = stringResource(Res.string.select_language), // Texto desde XML
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            HorizontalDivider(color = Color(0xFF444444))

            supportedLanguages.forEach { (code, name) ->
                val isSelected = code == currentLanguageCode
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected(code) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = name, // Mantenemos el nombre nativo del idioma
                        color = if (isSelected) Color(0xFF8687E7) else Color.White,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                    if (isSelected) {
                        Text("✓", color = Color(0xFF8687E7), fontSize = 20.sp)
                    }
                }
                HorizontalDivider(color = Color(0xFF444444))
            }
        }
    }
}