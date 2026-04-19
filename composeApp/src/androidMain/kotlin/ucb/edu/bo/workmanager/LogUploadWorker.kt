package ucb.edu.bo.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    override suspend fun doWork(): Result {
        println("✅ WorkManager ejecutando tarea en segundo plano")
        // Aquí puedes llamar un UseCase si quieres
        return Result.success()
    }
}