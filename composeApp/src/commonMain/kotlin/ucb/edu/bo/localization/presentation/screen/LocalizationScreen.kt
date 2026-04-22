package ucb.edu.bo.localization.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.app_name
import appmovil.composeapp.generated.resources.button_cancel
import appmovil.composeapp.generated.resources.button_save
import appmovil.composeapp.generated.resources.welcome_message

import com.example.designsystem.components.button.PrimaryButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun LocalizationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.welcome_message),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(text = stringResource(Res.string.button_save), onClick = {})

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = {}) {
            Text(stringResource(Res.string.button_cancel))
        }
    }
}