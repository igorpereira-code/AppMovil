package ucb.edu.bo.todoApp.task.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.add_image
import appmovil.composeapp.generated.resources.calendar
import appmovil.composeapp.generated.resources.home_2
import appmovil.composeapp.generated.resources.timer
import appmovil.composeapp.generated.resources.user
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavBar(
    onAddClick: () -> Unit,
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
                label = "Index",
                isSelected = true
            )
            // Calendar
            NavItem(
                iconRes = Res.drawable.calendar,
                label = "Calendario",
                isSelected = false
            )

            // FAB central (botón +)
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PrimaryPurple,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_image),
                    contentDescription = "Agregar tarea",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Focus
            NavItem(
                iconRes = Res.drawable.timer,
                label = "Focus",
                isSelected = false
            )
            // Profile
            NavItem(
                iconRes = Res.drawable.user,
                label = "Perfil",
                isSelected = false
            )
        }
    }
}

@Composable
private fun NavItem(iconRes: DrawableResource, label: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            tint = if (isSelected) PrimaryPurple else Color(0xFF888888),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) PrimaryPurple else Color(0xFF888888),
            fontSize = 10.sp
        )
    }
}