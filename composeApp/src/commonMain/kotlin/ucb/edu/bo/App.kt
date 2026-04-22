package ucb.edu.bo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import ucb.edu.bo.di.getModules
import ucb.edu.bo.dollar.presentation.screen.DollarScreen
import ucb.edu.bo.formulario.presentation.screen.FormularioScreen
import ucb.edu.bo.localization.presentation.screen.LocalizationScreen
import ucb.edu.bo.pushnotification.presentation.screen.PushNotificationScreen
import ucb.edu.bo.realtimedatabasecmp.presentation.screen.FirebaseTestScreen
import ucb.edu.bo.remoteconfig.presentation.screen.RemoteConfigScreen

@Composable
@Preview
fun App(){
        MaterialTheme {
            //FirebaseTestScreen()
            //DollarScreen()
            //PushNotificationScreen()
            //LocalizationScreen()
            RemoteConfigScreen()
            //FormularioScreen()
        }
}