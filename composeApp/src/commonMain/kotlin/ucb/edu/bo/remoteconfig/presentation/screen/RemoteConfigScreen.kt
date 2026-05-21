package ucb.edu.bo.remoteconfig.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.remoteconfig.presentation.viewmodel.RemoteConfigViewModel

@Composable
fun RemoteConfigScreen(
    viewModel: RemoteConfigViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Remote Config + Room", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = if (state.isFromCache) "📦 Desde caché local:" else "☁️ Desde Firebase:",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.value.ifEmpty { "Sin valor" },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}