package ucb.edu.bo.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.events.domain.usecase.LogAndSyncAppEventUseCase

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val logAndSyncUseCase: LogAndSyncAppEventUseCase by inject()

    override suspend fun doWork(): Result {
        println("WorkManager ejecutando tarea en segundo plano")

        val eventType = inputData.getString("EVENT_TYPE")

        if (eventType != null) {
            logAndSyncUseCase(eventType)
        }
        return Result.success()
    }
}