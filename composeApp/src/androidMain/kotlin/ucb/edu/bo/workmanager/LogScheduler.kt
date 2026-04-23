package ucb.edu.bo.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class LogScheduler(private val context: Context) {
    private val LOG_WORKNAME = "logUploadWork"
    private val INTERVAL_MINUTES = 15L // Mínimo permitido por Android

    fun schedulePeriodicUpload() {
        // Configuramos para que solo se ejecute si hay internet
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val logRequest = PeriodicWorkRequestBuilder<LogUploadWorker>(
            INTERVAL_MINUTES,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context.applicationContext)
            .enqueueUniquePeriodicWork(
                LOG_WORKNAME,
                ExistingPeriodicWorkPolicy.KEEP,
                logRequest
            )
    }
}