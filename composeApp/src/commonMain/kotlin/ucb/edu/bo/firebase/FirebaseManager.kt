package ucb.edu.bo.firebase

import ucb.edu.bo.todoApp.task.domain.model.TaskModel

/**
 * MODIFICADO: Se declaran los 3 nuevos métodos para la sincronización delta.
 * Actualiza tu FirebaseManager.kt (commonMain) con este contenido.
 */
expect class FirebaseManager() {
    suspend fun saveData(path: String, value: String)

    // NUEVOS para el Ejercicio #4
    suspend fun uploadTask(task: TaskModel, hash: String)
    suspend fun fetchRemoteHashes(): Map<Int, String>
    suspend fun fetchRemoteTasks(): List<TaskModel>
}
