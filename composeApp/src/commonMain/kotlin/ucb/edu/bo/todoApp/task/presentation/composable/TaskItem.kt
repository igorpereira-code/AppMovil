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
import appmovil.composeapp.generated.resources.Design
import appmovil.composeapp.generated.resources.Grocery
import appmovil.composeapp.generated.resources.Health
import appmovil.composeapp.generated.resources.Home
import appmovil.composeapp.generated.resources.Movie
import appmovil.composeapp.generated.resources.Music
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.Social
import appmovil.composeapp.generated.resources.Sport
import appmovil.composeapp.generated.resources.University
import appmovil.composeapp.generated.resources.Work
import appmovil.composeapp.generated.resources.flag
import org.jetbrains.compose.resources.painterResource

@Composable
fun TaskItem(
    title: String,
    timeText: String, // Ej: "Today At 16:45"
    priority: Int,
    categoryName: String? = null, // Ej: "University", "Home", "Work"
    categoryColor: Color = MaterialTheme.colorScheme.primary, // Color de fondo de la etiqueta
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onClick: () -> Unit // Para abrir la pantalla de detalles de la tarea
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF363636)), // Color del diseño
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
            // ── Círculo de Completado (Radio Button Custom) ──────────────
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
                        // Un pequeño punto blanco o check si está completado
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // ── Textos y Etiquetas ───────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {

                // Título
                Text(
                    text = title,
                    color = if (isCompleted) Color(0xFF888888) else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Fila Inferior: Hora + Categoría + Prioridad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Texto de la Hora
                    Text(
                        text = timeText,
                        color = Color(0xFFAFAFAF),
                        fontSize = 14.sp,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Etiqueta de Categoría (Se dibuja si tu compañero ya la definió)
                    if (categoryName != null) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(categoryColor)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Asignamos un ícono básico según el nombre
                            val icon = when (categoryName.lowercase()) {
                                "university" -> painterResource(Res.drawable.University)
                                "home" -> painterResource(Res.drawable.Home)
                                "work" -> painterResource(Res.drawable.Work)
                                "design" -> painterResource(Res.drawable.Design)
                                "sport" -> painterResource(Res.drawable.Sport)
                                "grocery" -> painterResource(Res.drawable.Grocery)
                                "health" -> painterResource(Res.drawable.Health)
                                "movie" -> painterResource(Res.drawable.Movie)
                                "music" -> painterResource(Res.drawable.Music)
                                "social" -> painterResource(Res.drawable.Social)

                                else -> painterResource(Res.drawable.Home)
                            }

                            Icon(
                                painter = icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = categoryName,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Etiqueta de Prioridad (Bandera)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp)) // Borde morado como en tu diseño
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