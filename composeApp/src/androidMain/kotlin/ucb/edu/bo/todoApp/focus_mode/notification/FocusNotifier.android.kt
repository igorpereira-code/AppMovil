package ucb.edu.bo.todoApp.focus_mode.notification

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.todoApp.focus_mode.data.service.FocusTimerService

actual class FocusNotifier actual constructor() : KoinComponent {

    private val context: Context by inject()

    actual fun notify(minutes: Int) {
        FocusNotificationHelper.sendFocusCompletedNotification(context, minutes)
    }

    actual fun startTimerNotification(timeText: String) {
        val intent = Intent(context, FocusTimerService::class.java).apply {
            action = "START"
            putExtra("TIME", timeText)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    // ACTUALIZA EL TEXTO
    actual fun updateTimerNotification(timeText: String) {
        val intent = Intent(context, FocusTimerService::class.java).apply {
            action = "UPDATE"
            putExtra("TIME", timeText)
        }
        context.startService(intent)
    }

    // MATA EL SERVICIO
    actual fun stopTimerNotification() {
        val intent = Intent(context, FocusTimerService::class.java).apply {
            action = "STOP"
        }
        context.startService(intent)
    }
}