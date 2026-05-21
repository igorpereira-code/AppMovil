package ucb.edu.bo.formulario.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.formulario.presentation.viewmodel.FormularioViewModel

@Composable
fun FormularioScreen(
    viewModel: FormularioViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Formulario",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.mensaje,
            onValueChange = { viewModel.onMensajeChange(it) },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.lastSaved.isNotEmpty()) {
            Text(
                text = state.lastSaved,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { viewModel.saveLocal() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 Guardar borrador")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.syncToFirebase() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Enviar y sincronizar con Firebase")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.successMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        state.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}