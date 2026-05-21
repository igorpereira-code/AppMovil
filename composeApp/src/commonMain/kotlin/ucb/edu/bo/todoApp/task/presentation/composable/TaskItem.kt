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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

@Composable
fun TaskItem(
    title: String,
    description: String,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggle, modifier = Modifier.size(28.dp)) {
                RadioButton(
                    selected = isCompleted,
                    onClick = onToggle,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = PrimaryPurple,
                        unselectedColor = Color(0xFF666666)
                    )
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isCompleted) GrayText else Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        color = GrayText,
                        fontSize = 12.sp,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                //Icon(
                    //painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                    //contentDescription = "Eliminar",
                    //tint = Color(0xFF666666),
                    //modifier = Modifier.size(16.dp)
                //)
            }
        }
    }
}