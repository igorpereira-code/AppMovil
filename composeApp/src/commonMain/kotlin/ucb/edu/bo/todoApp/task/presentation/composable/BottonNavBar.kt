package ucb.edu.bo.todoApp.task.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavBar(
    currentRoute: String, // NUEVO: Para saber qué ícono pintar de morado
    onHomeClick: () -> Unit, // NUEVO: Callback para el index
    onCalendarClick: () -> Unit, // NUEVO: Callback para el calendario
    onFocusClick: () -> Unit, // NUEVO: Callback para el focus
    onAddClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1D1D1D))
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Index (Home)
            NavItem(
                iconRes = Res.drawable.home_2,
                label = stringResource(Res.string.nav_index),
                isSelected = currentRoute == "Index", // Dinámico
                onClick = onHomeClick
            )
            // Calendar
            NavItem(
                iconRes = Res.drawable.calendar,
                label = stringResource(Res.string.nav_calendar),
                isSelected = currentRoute == "Calendario", // Dinámico
                onClick = onCalendarClick
            )

            // FAB central (botón +)
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_image),
                    contentDescription = stringResource(Res.string.nav_cd_add_task),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            NavItem(
                iconRes = Res.drawable.timer,
                label = stringResource(Res.string.nav_focus),
                isSelected = currentRoute == "Focus",
                onClick = onFocusClick
            )
            // Profile (Callbacks pendientes para tu equipo)
            NavItem(
                iconRes = Res.drawable.user,
                label = stringResource(Res.string.nav_profile),
                isSelected = currentRoute == "Perfil",
                onClick = { onProfileClick() }
            )
        }
    }
}

@Composable
private fun NavItem(
    iconRes: DrawableResource,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit // NUEVO: Parámetro para recibir el clic
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)) // Hace que el efecto de onda al tocar sea redondeado
            .clickable { onClick() }
            .padding(4.dp) // Aumenta sutilmente el área táctil
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF888888),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF888888),
            fontSize = 10.sp
        )
    }
}