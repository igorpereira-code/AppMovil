package ucb.edu.bo.remoteconfig

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MaintenanceGate(
    viewModel: MaintenanceViewModel = koinViewModel(),
    content: @Composable () -> Unit
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is MaintenanceState.Loading -> LoadingScreen()
        is MaintenanceState.UnderMaintenance -> MaintenanceScreen()
        is MaintenanceState.Operational,
        is MaintenanceState.Error -> content()
    }
}

@Composable
private fun LoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(animation = tween(800), repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    Box(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF0F0F1A), Color(0xFF1A1A2E)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.size(56.dp).scale(scale), color = Color(0xFF6C63FF), strokeWidth = 4.dp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Iniciando aplicación…", color = Color.White.copy(alpha = 0.7f), fontSize = 15.sp)
        }
    }
}

@Composable
private fun MaintenanceScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF0F0F1A), Color(0xFF1A1A2E)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            val infiniteTransition = rememberInfiniteTransition(label = "gear")
            val alpha by infiniteTransition.animateFloat(initialValue = 0.4f, targetValue = 1f, animationSpec = infiniteRepeatable(animation = tween(1200), repeatMode = RepeatMode.Reverse), label = "alpha")

            Box(modifier = Modifier.size(96.dp).background(color = Color(0xFF6C63FF).copy(alpha = alpha * 0.25f), shape = CircleShape), contentAlignment = Alignment.Center) {
                Text(text = "🔧", fontSize = 48.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "En Mantenimiento", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Estamos mejorando la aplicación.\nVuelve en unos momentos. 🚀", fontSize = 15.sp, color = Color.White.copy(alpha = 0.7f), textAlign = TextAlign.Center, lineHeight = 22.sp)
        }
    }
}
