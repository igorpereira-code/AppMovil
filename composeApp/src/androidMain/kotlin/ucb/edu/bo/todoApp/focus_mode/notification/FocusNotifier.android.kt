package ucb.edu.bo.todoApp.focus_mode.notification

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class FocusNotifier actual constructor() : KoinComponent {

    private val context: Context by inject()

    actual fun notify(minutes: Int) {
        FocusNotificationHelper.sendFocusCompletedNotification(context, minutes)
    }
}