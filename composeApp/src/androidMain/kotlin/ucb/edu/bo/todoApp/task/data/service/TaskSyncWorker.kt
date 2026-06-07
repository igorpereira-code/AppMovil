package ucb.edu.bo.todoApp.task.data.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.firebase.FirebaseManager
import ucb.edu.bo.todoApp.task.data.util.TaskHashUtil
import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository

/**
 * EJERCICIO #4 — Sincronización delta con compresión diferencial
 *
 * Flujo completo:
 *  1. Muestra notificación de progreso
 *  2. Obtiene todos los hashes remotos de Firebase (una sola lectura)
 *  3. Por cada tarea local: calcula hash local y compara con el remoto
 *     → Si es igual: SKIP (no transfiere datos)
 *     → Si es diferente o no existe: SUBE la tarea + actualiza hash
 *  4. Descarga tareas de Firebase que no existen en Room (sync bidireccional)
 *  5. Marca las tareas como sincronizadas en Room
 *  6. Muestra notificación con el resumen (X subidas, Y sin cambios, Z descargadas)
 *
 * Este worker reemplaza completamente tu TaskSyncWorker.kt existente.
 */
class TaskSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val taskRepository: TaskRepository by inject()
    private val firebaseManager: FirebaseManager by inject()

    override suspend fun doWork(): Result {
        // ── 1. Mostrar notificación de progreso ───────────────────────────
        android.util.Log.d("SyncDelta", "=== WORKER INICIADO ===")
        SyncNotificationHelper.createChannel(applicationContext)
        SyncNotificationHelper.showProgress(applicationContext)

        return try {
            // ── 2. Obtener todos los hashes remotos en una sola lectura ───
            android.util.Log.d("SyncDelta", "Obteniendo hashes remotos...")
            val remoteHashes: Map<Int, String> = firebaseManager.fetchRemoteHashes()
            android.util.Log.d("SyncDelta", "Hashes remotos obtenidos: ${remoteHashes.size}")

            // ── 3. Procesar tareas locales con compresión diferencial ──────
            val localTasks = taskRepository.getAll()
            android.util.Log.d("SyncDelta", "Tareas locales: ${localTasks.size}")


            var uploaded = 0
            var skipped  = 0
            val uploadedIds = mutableListOf<Int>()

            for (task in localTasks) {
                val localHash  = TaskHashUtil.computeHash(task)
                val remoteHash = remoteHashes[task.id]

                if (localHash == remoteHash) {
                    // ✅ Hash idéntico → sin cambios, no transferir
                    skipped++
                } else {
                    Log.d("TaskSyncWorker", "Subiendo tarea ID: ${task.id} (Hash cambió)")
                    firebaseManager.uploadTask(task, localHash)
                    uploadedIds.add(task.id)
                    uploaded++
                }
            }

            // ── 4. Marcar tareas subidas como sincronizadas en Room ────────
            uploadedIds.forEach { id ->
                taskRepository.markAsSynced(id)
            }

            // Sync bidireccional
            val remoteTasks = firebaseManager.fetchRemoteTasks()
            val localIds = localTasks.map { it.id }.toSet()
            var downloaded = 0
            for (remoteTask in remoteTasks) {
                if (remoteTask.id !in localIds) {
                    taskRepository.save(remoteTask)
                    downloaded++
                }
            }

            // ── 6. Notificación de éxito con resumen ──────────────────────
            SyncNotificationHelper.showSuccess(
                context    = applicationContext,
                uploaded   = uploaded,
                skipped    = skipped,
                downloaded = downloaded
            )
            
            Log.d("TaskSyncWorker", "Sincronización terminada: $uploaded subidas, $downloaded descargadas")
            Result.success()

        } catch (e: Exception) {
            Log.e("TaskSyncWorker", "Error crítico: ${e.message}", e)
            SyncNotificationHelper.showError(
                context = applicationContext,
                reason  = e.message ?: "Error desconocido"
            )
            Result.retry()
        }
    }
}
