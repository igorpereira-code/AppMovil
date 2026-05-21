package ucb.edu.bo.todoApp.task.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavBar(onAddClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().background(SurfaceDark).padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(android.R.drawable.ic_menu_today, "Index", true)
            NavItem(android.R.drawable.ic_menu_my_calendar, "Calendario", false)
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PrimaryPurple,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(painterResource(android.R.drawable.ic_input_add), "Agregar", tint = Color.White)
            }
            NavItem(android.R.drawable.ic_menu_recent_history, "Focus", false)
            NavItem(android.R.drawable.ic_menu_myplaces, "Perfil", false)
        }
    }
}

@Composable
private fun NavItem(iconRes: Int, label: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = if (isSelected) PrimaryPurple else GrayText,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = if (isSelected) PrimaryPurple else GrayText, fontSize = 10.sp)
    }
}