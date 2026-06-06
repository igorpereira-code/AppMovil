package ucb.edu.bo.todoApp.calendar.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.sort_image // Puedes cambiar esto por íconos de flechas
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource

@Composable
fun CalendarCarousel(
    monthYearText: String, // ej. "FEBRUARY \n 2022"
    weekDays: List<LocalDate>, // Lista de exactamente 7 días
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1D1D1D)) // Usando tu SurfaceDark
            .padding(vertical = 16.dp)
    ) {
        // Cabecera: Mes, Año y Flechas
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousWeek) {
                Text("<", color = Color.White, fontSize = 20.sp) // Reemplazar con Icono
            }

            Text(
                text = monthYearText,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            IconButton(onClick = onNextWeek) {
                Text(">", color = Color.White, fontSize = 20.sp) // Reemplazar con Icono
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Carrusel de 7 días
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekDays.forEach { date ->
                val isSelected = date == selectedDate
                val dayOfWeek = date.dayOfWeek.name.take(3) // Ej. "MON"
                val isWeekend = dayOfWeek == "SUN" || dayOfWeek == "SAT"

                Column(
                    modifier = Modifier
                        .width(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF272727))
                        .clickable { onDateSelected(date) }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dayOfWeek,
                        color = if (isWeekend && !isSelected) Color(0xFFFF4C4C) else if (isSelected) Color.White else Color(0xFF888888),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}