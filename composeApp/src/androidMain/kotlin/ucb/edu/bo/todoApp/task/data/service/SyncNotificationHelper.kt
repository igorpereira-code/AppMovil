package ucb.edu.bo.todoApp.task.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import appmovil.composeapp.generated.resources.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

/**
 * Helper de notificaciones para el servicio de sincronización delta.
 */
object SyncNotificationHelper {

    private const val CHANNEL_ID    = "task_sync_channel"
    private const val CHANNEL_NAME  = "Sincronización de Tareas"
    private const val NOTIF_PROGRESS = 2001
    private const val NOTIF_RESULT   = 2002

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Progreso y resultado de sincronización offline→Firebase"
            }
            context.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    fun showProgress(context: Context) {
        val statusText = runBlocking { getString(Res.string.notification_sync_checking) }
        val titleText = "Sincronizando tareas…" // Not in strings.xml, keeping or could add if I had permission to edit strings.xml

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setContentTitle(titleText)
            .setContentText(statusText)
            .setProgress(0, 0, true)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_PROGRESS, notif)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun showSuccess(context: Context, uploaded: Int, skipped: Int, downloaded: Int) {
        NotificationManagerCompat.from(context).cancel(NOTIF_PROGRESS)

        val text = "↑ $uploaded subidas · ↓ $downloaded descargadas · $skipped sin cambios"

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("✅ Sincronización completada")
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_RESULT, notif)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun showError(context: Context, reason: String) {
        NotificationManagerCompat.from(context).cancel(NOTIF_PROGRESS)

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⚠️ Error al sincronizar")
            .setContentText(reason)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_RESULT, notif)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
