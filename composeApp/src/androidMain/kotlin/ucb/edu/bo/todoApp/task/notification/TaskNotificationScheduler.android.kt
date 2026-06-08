package ucb.edu.bo.todoApp.task.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Locale

actual class TaskNotificationScheduler actual constructor() : KoinComponent {

    // Obtenemos el contexto global de Android gracias a Koin
    private val context: Context by inject()

    actual fun scheduleNotification(taskId: Int, title: String, timeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
            putExtra("EXTRA_TASK_ID", taskId)
            putExtra("EXTRA_TITLE", title)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Programa la alarma exacta para despertar al teléfono
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    actual fun cancelNotification(taskId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TaskAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}

actual fun convertDateTimeToMillis(date: String, time: String): Long {
    return try {
        // Asumiendo que tu fecha es "2026-05-19" y tu hora "16:45"
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateObj = format.parse("$date $time")
        dateObj?.time ?: 0L
    } catch (e: Exception) {
        0L // Si hay error al convertir, retorna 0
    }
}