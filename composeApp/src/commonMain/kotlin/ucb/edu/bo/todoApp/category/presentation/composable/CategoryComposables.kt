package ucb.edu.bo.todoApp.category.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

// Función global para leer las imágenes correctamente en cualquier parte
fun getCategoryIconResource(iconName: String?): DrawableResource {
    return when (iconName?.lowercase()) {
        "school" -> Res.drawable.school
        "home" -> Res.drawable.home_2
        "social" -> Res.drawable.social
        "game" -> Res.drawable.game
        "exercise" -> Res.drawable.exercise
        "food" -> Res.drawable.food
        "heart" -> Res.drawable.heart
        "cake" -> Res.drawable.cake
        "add_image" -> Res.drawable.add_image
        else -> Res.drawable.home_2
    }
}

@Composable
fun CategoryGridItem(
    name: String,
    colorHex: Long,
    iconName: String,
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
            Icon(
                painter = painterResource(getCategoryIconResource(iconName)),
                contentDescription = null,
                tint = if (colorHex == 0xFF00FFCC) Color.Black else Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp
        )
    }
}
