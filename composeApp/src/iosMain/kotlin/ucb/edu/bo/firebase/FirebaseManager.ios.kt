package ucb.edu.bo.firebase

import ucb.edu.bo.todoApp.task.domain.model.TaskModel

actual class FirebaseManager actual constructor() {
       actual suspend fun saveData(path: String, value: String) {}
       actual suspend fun uploadTask(task: TaskModel, hash: String) {}
       actual suspend fun fetchRemoteHashes(): Map<Int, String> = emptyMap()
       actual suspend fun fetchRemoteTasks(): List<TaskModel> = emptyList()
   }