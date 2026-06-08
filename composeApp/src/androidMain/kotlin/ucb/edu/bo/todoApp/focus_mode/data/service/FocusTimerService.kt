package ucb.edu.bo.todoApp.focus_mode.data.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import appmovil.composeapp.generated.resources.focus_timer_notification_body
import appmovil.composeapp.generated.resources.focus_timer_notification_title
import ucb.edu.bo.todoApp.focus_mode.notification.FocusNotificationHelper

class FocusTimerService : Service() {
    private val NOTIFICATION_ID = 888

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val timeText = intent?.getStringExtra("TIME") ?: "00:00"

        when (action) {
            "START" -> {
                // Aseguramos que el canal de tu Helper exista
                FocusNotificationHelper.createNotificationChannel(this)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startForeground(
                        NOTIFICATION_ID,
                        buildNotification(timeText),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                    )
                } else {
                    startForeground(NOTIFICATION_ID, buildNotification(timeText))
                }
            }
            "UPDATE" -> {
                val manager = getSystemService(NotificationManager::class.java)
                manager.notify(NOTIFICATION_ID, buildNotification(timeText))
            }
            "STOP" -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }
    private fun buildNotification(timeText: String): Notification {
        // Obtenemos los textos usando runBlocking ya que estamos en una clase clásica de Android
        val title = kotlinx.coroutines.runBlocking {
            org.jetbrains.compose.resources.getString(appmovil.composeapp.generated.resources.Res.string.focus_timer_notification_title)
        }
        val body = kotlinx.coroutines.runBlocking {
            org.jetbrains.compose.resources.getString(appmovil.composeapp.generated.resources.Res.string.focus_timer_notification_body, ":$timeText")
        }

        return NotificationCompat.Builder(this, "focus_mode_channel")
            .setContentTitle(title)
            .setContentText("$timeText")
            .setSmallIcon(applicationInfo.icon)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}