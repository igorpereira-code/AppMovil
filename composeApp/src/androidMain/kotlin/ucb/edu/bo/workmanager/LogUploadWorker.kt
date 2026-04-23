package ucb.edu.bo.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.events.domain.usecase.ClearEventsUseCase
import ucb.edu.bo.events.domain.usecase.GetEventListUseCase
import ucb.edu.bo.realtimedatabasecmp.data.datasource.FirebaseDataSource

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    // Inyectamos los casos de uso y el datasource de Firebase
    private val getEventsUseCase: GetEventListUseCase by inject()
    private val clearEventsUseCase: ClearEventsUseCase by inject()
    private val firebaseDataSource: FirebaseDataSource by inject()

    override suspend fun doWork(): Result {
        return try {
            // 1. Obtenemos los eventos que guardamos en Room (OPEN/CLOSE)
            val events = getEventsUseCase()

            if (events.isNotEmpty()) {
                // 2. Los subimos a Firebase Realtime Database
                events.forEach { event ->
                    firebaseDataSource.saveTestValue("logs/${event.timestamp}", event.type)
                }
                // 3. Si todo salió bien, borramos los eventos locales para no duplicar
                clearEventsUseCase()
            }

            println("✅ Sincronización de eventos completada con éxito")
            Result.success()
        } catch (e: Exception) {
            println("❌ Error en WorkManager: ${e.message}")
            Result.retry() // Reintenta más tarde si falla el internet
        }
    }
}