package ucb.edu.bo.app.home.presentation.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.calendar
import appmovil.composeapp.generated.resources.clock
import appmovil.composeapp.generated.resources.home_2
import appmovil.composeapp.generated.resources.user
import org.jetbrains.compose.resources.painterResource


@Composable
fun HomeBottomNavigation() {
    NavigationBar(
        containerColor = Color(0xFF363636),
        contentColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier.height(100.dp)
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(painterResource(Res.drawable.home_2), "Index", modifier = Modifier.size(30.dp)) },
            label = { Text("Index", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(painterResource(Res.drawable.calendar), "Calendar", modifier = Modifier.size(30.dp)) },
            label = { Text("Calendar", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.weight(0.5f))
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(painterResource(Res.drawable.clock), "Focuse", modifier = Modifier.size(30.dp)) },
            label = { Text("Focuse", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(painterResource(Res.drawable.user), "Profile", modifier = Modifier.size(30.dp)) },
            label = { Text("Profile", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
    }
}
