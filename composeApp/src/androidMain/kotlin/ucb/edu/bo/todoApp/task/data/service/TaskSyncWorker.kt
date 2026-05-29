package ucb.edu.bo.todoApp.task.data.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository
import ucb.edu.bo.firebase.FirebaseManager // Asegúrate de ajustar esta importación a tu clase real

class TaskSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    // Inyectamos de forma segura las herramientas necesarias desde Koin
    private val taskRepository: TaskRepository by inject()
    // Si tu FirebaseManager maneja la escritura en la BD en tiempo real, lo inyectamos aquí
    private val firebaseManager: FirebaseManager by inject()

    override suspend fun doWork(): Result {
        return try {
            // 1. Obtener tareas locales no sincronizadas desde la fuente local
            // Puedes exponer la lista de "Unsynced" a través del repositorio
            val unsyncedTasks = taskRepository.getAll().filter { !it.isCompleted } // O implementar el método directo en tu flujo de datos

            if (unsyncedTasks.isEmpty()) return Result.success()

            // 2. Subir cada tarea pendiente a Firebase Realtime Database
            unsyncedTasks.forEach { task ->
                // Ejemplo analítico de envío a Firebase a través de tu estructura de red/nube
                // firebaseManager.uploadTask(task)

                // 3. Si la subida fue exitosa, actualizamos el flag en Room localmente
                // taskRepository.markAsSynced(task.id)
            }

            // 4. Descargar cambios nuevos de Firebase e insertarlos en Room (Sincronización Bidireccional)
            // val remoteTasks = firebaseManager.fetchLatestTasks()
            // remoteTasks.forEach { taskRepository.save(it) }

            Result.success()
        } catch (e: Exception) {
            // Si ocurre un error de red, WorkManager reintentará el trabajo automáticamente más tarde
            Result.retry()
        }
    }
}