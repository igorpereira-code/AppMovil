package ucb.edu.bo.firebase

import ucb.edu.bo.todoApp.task.domain.model.TaskModel

/**
 * MODIFICADO: Se declaran los nuevos métodos para la sincronización delta y borrado.
 */
expect class FirebaseManager() {
    suspend fun saveData(path: String, value: String)

    // NUEVOS para el Ejercicio #4
    suspend fun uploadTask(task: TaskModel, hash: String)
    suspend fun fetchRemoteHashes(): Map<Int, String>
    suspend fun fetchRemoteTasks(): List<TaskModel>
    
    // NUEVO: Borrado remoto
    suspend fun deleteTask(taskId: Int)
}
