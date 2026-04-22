package ucb.edu.bo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.KoinAppDeclaration
import ucb.edu.bo.config.presentation.screen.SyncConfigScreen
import ucb.edu.bo.di.getModules
import ucb.edu.bo.dollar.presentation.screen.DollarScreen
import ucb.edu.bo.events.presentation.viewmodel.AppEventViewModel
import ucb.edu.bo.localization.presentation.screen.LocalizationScreen
import ucb.edu.bo.pushnotification.presentation.screen.PushNotificationScreen
import ucb.edu.bo.realtimedatabasecmp.presentation.screen.FirebaseTestScreen
import ucb.edu.bo.remoteconfig.presentation.screen.RemoteConfigScreen

@Composable
@Preview
fun App(){
    val appEventViewModel = koinViewModel<AppEventViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current

    // 2. Escuchamos cuando la app se abre o se pausa
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                appEventViewModel.onAppOpened()
            } else if (event == Lifecycle.Event.ON_STOP) {
                appEventViewModel.onAppClosed()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
        MaterialTheme {
            //FirebaseTestScreen()
            //DollarScreen()
            //PushNotificationScreen()
            //RemoteConfigScreen()
            //LocalizationScreen()
            SyncConfigScreen()
        }
}