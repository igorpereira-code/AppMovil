package ucb.edu.bo.todoApp.task.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun TaskItem(
    title: String,
    timeText: String,
    priority: Int,
    categoryName: String? = null,
    categoryIcon: String? = null, // NUEVO: Parámetro para recibir la imagen exacta
    categoryColor: Color = MaterialTheme.colorScheme.primary,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF363636)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggle, modifier = Modifier.size(28.dp)) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .border(
                            width = 2.dp,
                            color = if (isCompleted) MaterialTheme.colorScheme.primary else Color(0xFF888888),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color.White))
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isCompleted) Color(0xFF888888) else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = timeText,
                        color = Color(0xFFAFAFAF),
                        fontSize = 14.sp,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Etiqueta de Categoría
                    if (categoryName != null) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(categoryColor)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // AQUÍ ESTÁ LA CORRECCIÓN: Leemos el categoryIcon y no el name
                            val icon = when (categoryIcon?.lowercase()) {
                                "school" -> painterResource(Res.drawable.school)
                                "home" -> painterResource(Res.drawable.home)
                                "social" -> painterResource(Res.drawable.social)
                                "game" -> painterResource(Res.drawable.game)
                                "exercise" -> painterResource(Res.drawable.exercise)
                                "food" -> painterResource(Res.drawable.food)
                                "heart" -> painterResource(Res.drawable.heart)
                                "cake" -> painterResource(Res.drawable.cake)
                                else -> painterResource(Res.drawable.home)
                            }

                            Icon(
                                painter = icon,
                                contentDescription = null,
                                tint = Color.Black, // Color del ícono y texto (Negro sobre colores claros)
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = categoryName,
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Etiqueta de Prioridad
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.flag),
                            contentDescription = "Prioridad",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = priority.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}