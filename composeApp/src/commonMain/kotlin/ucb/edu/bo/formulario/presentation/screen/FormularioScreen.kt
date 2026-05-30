package ucb.edu.bo.formulario.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.formulario.presentation.viewmodel.FormularioViewModel
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

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
            text = stringResource(Res.string.form_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Note: I'll use common labels or specific ones if available in strings.xml
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            label = { Text(stringResource(Res.string.form_label_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.mensaje,
            onValueChange = { viewModel.onMensajeChange(it) },
            label = { Text(stringResource(Res.string.form_label_lastname)) }, // Reusing or matching what's in strings.xml
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
            Text(stringResource(Res.string.common_save))
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
                Text(stringResource(Res.string.form_button_submit))
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