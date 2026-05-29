
package ucb.edu.bo.todoApp.category.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryGridItem(
    name: String,
    colorHex: Long,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(colorHex), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Aquí irá el Icono, por ahora un placeholder
            Text(text = name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, color = Color.White, fontSize = 12.sp)
    }
}