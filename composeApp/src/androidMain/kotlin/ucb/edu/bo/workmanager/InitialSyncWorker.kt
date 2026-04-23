package ucb.edu.bo.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.dollar.domain.model.DollarModel
import ucb.edu.bo.dollar.domain.usecase.CreateDollarUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.FetchRemoteConfigUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.GetRemoteStringUseCase

class InitialSyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams), KoinComponent {

    private val fetchRemoteConfig: FetchRemoteConfigUseCase by inject()
    private val getRemoteString: GetRemoteStringUseCase by inject()
    private val createDollar: CreateDollarUseCase by inject()

    override suspend fun doWork(): Result {
        return try {
            // Descarga inicial en segundo plano
            val success = fetchRemoteConfig()
            if (success) {
                val value = getRemoteString("welcome_message")
                // Guardamos en Room para que el ViewModel lo use después
                createDollar(DollarModel(dollarOfficial = value, dollarParallel = "FROM_REMOTE"))
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}