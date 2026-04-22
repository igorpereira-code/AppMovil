package ucb.edu.bo.remoteconfig.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.remoteconfig.domain.usecase.SyncRemoteConfigUseCase

class RemoteConfigSyncWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val syncRemoteConfigUseCase: SyncRemoteConfigUseCase by inject()

    override suspend fun doWork(): Result {
        return try {
            val result = syncRemoteConfigUseCase("welcome_message")
            if (result.isSuccess) {
                println("✅ RemoteConfig sincronizado: ${result.getOrNull()}")
                Result.success()
            } else {
                println("❌ Error sincronizando RemoteConfig")
                Result.retry()
            }
        } catch (e: Exception) {
            println("❌ Excepción: ${e.message}")
            Result.failure()
        }
    }
}