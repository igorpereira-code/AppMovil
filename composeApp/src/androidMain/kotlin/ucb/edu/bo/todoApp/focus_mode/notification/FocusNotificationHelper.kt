package ucb.edu.bo.todoApp.focus_mode.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.*

object FocusNotificationHelper {

    private const val CHANNEL_ID_TIMER = "focus_timer_channel"
    private const val CHANNEL_ID_COMPLETED = "focus_completed_channel"
    private const val CHANNEL_NAME_TIMER = "Temporizador Enfoque"
    private const val CHANNEL_NAME_COMPLETED = "Sesión Completada"
    private const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Canal SIN sonido para el timer
            val timerChannel = NotificationChannel(
                CHANNEL_ID_TIMER,
                CHANNEL_NAME_TIMER,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Muestra el tiempo restante del modo enfoque"
                setSound(null, null)
                enableVibration(false)
            }

            // Canal CON sonido solo para cuando se completa
            val completedChannel = NotificationChannel(
                CHANNEL_ID_COMPLETED,
                CHANNEL_NAME_COMPLETED,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifica cuando se completa una sesión de enfoque"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(timerChannel)
            manager.createNotificationChannel(completedChannel)
        }
    }

    fun getTimerChannelId() = CHANNEL_ID_TIMER
    fun getCompletedChannelId() = CHANNEL_ID_COMPLETED

    fun sendFocusCompletedNotification(context: Context, minutes: Int) {
        val title = runBlocking { getString(Res.string.notification_focus_title) }
        val body = runBlocking { getString(Res.string.notification_focus_body_minutes, minutes) }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_COMPLETED)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}