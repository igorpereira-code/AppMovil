package ucb.edu.bo.app.home.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.flag
import appmovil.composeapp.generated.resources.send
import appmovil.composeapp.generated.resources.tag
import appmovil.composeapp.generated.resources.timer
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddTaskSheetContent(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTimerClick: () -> Unit,
    onPriorityClick: () -> Unit,
    onSave: () -> Unit
) {
    Column(Modifier.padding(24.dp).fillMaxWidth().navigationBarsPadding()) {
        Text("Add Task", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        CustomTextField(value = title, onValueChange = onTitleChange, hint = "Do math homework")
        Spacer(Modifier.height(12.dp))
        CustomTextField(value = description, onValueChange = onDescriptionChange, hint = "Description")

        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onTimerClick) {
                Icon(painterResource(Res.drawable.timer), null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { /* TODO: Tag */ }) {
                Icon(painterResource(Res.drawable.tag), null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onPriorityClick) {
                Icon(painterResource(Res.drawable.flag), null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            Spacer(Modifier.weight(1f))

            IconButton(onClick = onSave) {
                Icon(
                    painter = painterResource(Res.drawable.send),
                    contentDescription = null, 
                    tint = Color(0xFF8875FF),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
