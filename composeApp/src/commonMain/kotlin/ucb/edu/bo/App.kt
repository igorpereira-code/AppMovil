package ucb.edu.bo

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.theme.DsTheme
import com.example.designsystem.theme.ThemeMode
import ucb.edu.bo.dollar.presentation.screen.DollarScreen
import ucb.edu.bo.localization.presentation.screen.LocalizationScreen
import ucb.edu.bo.pushnotification.presentation.screen.PushNotificationScreen
import ucb.edu.bo.realtimedatabasecmp.presentation.screen.FirebaseTestScreen
import ucb.edu.bo.remoteconfig.presentation.screen.RemoteConfigScreen

@Composable
@Preview
fun App() {
    DsTheme(mode = ThemeMode.LIGHT) {
        //FirebaseTestScreen()
        //DollarScreen()
        //PushNotificationScreen()
        //RemoteConfigScreen()
        LocalizationScreen()
    }
}