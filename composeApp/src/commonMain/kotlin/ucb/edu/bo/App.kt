package ucb.edu.bo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.designsystem.theme.AppTheme
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
import com.google.firebase.auth.FirebaseAuth
import org.koin.compose.koinInject
import ucb.edu.bo.todoApp.settings.domain.preferences.ISettingsPreferences
import ucb.edu.bo.utils.setAppLanguage

@Composable
fun App() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) {
        Screen.Task.route
    } else {
        Screen.Intro.route
    }
    // 1. Inyectamos las preferencias directamente en la raíz
    val settingsPreferences = koinInject<ISettingsPreferences>()

    // 2. Observamos el idioma en tiempo real (por defecto "en")
    val currentLanguage by settingsPreferences.getLanguage().collectAsState(initial = "en")
    val currentColorHex by settingsPreferences.getAppColor().collectAsState(initial = "FF8687E7")
    val currentTypography by settingsPreferences.getTypography().collectAsState(initial = "Default")

    // 3. Efecto Secundario: Cada vez que el idioma cambie, avisamos al sistema nativo
    LaunchedEffect(currentLanguage) {
        setAppLanguage(currentLanguage)
    }
    AppTheme(
        dynamicPrimaryColorHex = currentColorHex,
        dynamicTypography = currentTypography) {
        key(currentLanguage) {
            AppNavigation(startDestination = startDestination)
        }
    }
}