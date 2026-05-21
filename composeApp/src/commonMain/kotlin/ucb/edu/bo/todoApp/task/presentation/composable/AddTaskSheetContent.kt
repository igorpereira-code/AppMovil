package ucb.edu.bo.todoApp.task.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddTaskSheetContent(
    title: String, description: String, isSaving: Boolean, errorMessage: String?,
    onTitleChange: (String) -> Unit, onDescriptionChange: (String) -> Unit, onSend: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 24.dp)) {
        Text("Agregar Tarea", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(20.dp))

        val fieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryPurple, unfocusedBorderColor = Color(0xFF444444),
            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
            focusedContainerColor = SurfaceDark, unfocusedContainerColor = SurfaceDark
        )

        OutlinedTextField(title, onTitleChange, placeholder = { Text("Título", color = GrayText) },
            modifier = Modifier.fillMaxWidth(), colors = fieldColors, shape = RoundedCornerShape(8.dp))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(description, onDescriptionChange, placeholder = { Text("Descripción", color = GrayText) },
            modifier = Modifier.fillMaxWidth(), colors = fieldColors, shape = RoundedCornerShape(8.dp))

        errorMessage?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            // Iconos decorativos omitidos por brevedad, agrega los mismos que tenías
            Spacer(modifier = Modifier.weight(1f))
            if (isSaving) {
                CircularProgressIndicator(color = PrimaryPurple, modifier = Modifier.size(24.dp))
            } else {
                IconButton(onClick = onSend, enabled = title.isNotBlank(),
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(if (title.isNotBlank()) PrimaryPurple else Color(0xFF444444))) {
                    //Icon(painterResource(android.R.drawable.ic_menu_send), "Guardar", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}