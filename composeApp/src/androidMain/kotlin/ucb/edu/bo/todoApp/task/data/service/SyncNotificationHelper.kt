package ucb.edu.bo.todoApp.task.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Helper de notificaciones para el servicio de sincronización delta.
 * Muestra:
 *  - Notificación de progreso mientras sincroniza
 *  - Notificación de éxito con resumen (X subidas, Y sin cambios)
 *  - Notificación de error si falla
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
                NotificationManager.IMPORTANCE_LOW          // Baja para no interrumpir
            ).apply {
                description = "Progreso y resultado de sincronización offline→Firebase"
            }
            context.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    /** Muestra una notificación de progreso indeterminado mientras trabaja */
    fun showProgress(context: Context) {
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setContentTitle("Sincronizando tareas…")
            .setContentText("Verificando cambios pendientes")
            .setProgress(0, 0, true)             // Progreso indeterminado
            .setOngoing(true)                    // No se puede descartar
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_PROGRESS, notif)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    /**
     * Reemplaza la notificación de progreso con el resultado final.
     * @param uploaded  cuántas tareas se subieron a Firebase
     * @param skipped   cuántas se saltaron (hash igual = sin cambios)
     * @param downloaded cuántas se descargaron desde Firebase
     */
    fun showSuccess(context: Context, uploaded: Int, skipped: Int, downloaded: Int) {
        // Cancelar la de progreso
        NotificationManagerCompat.from(context).cancel(NOTIF_PROGRESS)

        val text = when {
            uploaded == 0 && downloaded == 0 ->
                "Todo al día — no hubo cambios ($skipped tareas verificadas)"
            else ->
                "↑ $uploaded subidas · ↓ $downloaded descargadas · $skipped sin cambios"
        }

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

    /** Muestra una notificación de error con mensaje */
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
