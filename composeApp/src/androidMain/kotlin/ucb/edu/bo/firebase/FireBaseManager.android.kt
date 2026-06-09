package ucb.edu.bo.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import ucb.edu.bo.todoApp.task.domain.model.TaskModel

/**
 * MODIFICADO: Se agregan uploadTask, fetchRemoteHashes, fetchRemoteTasks y deleteTask
 * para soportar la sincronización delta y el borrado remoto.
 */
actual class FirebaseManager actual constructor() {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    actual suspend fun saveData(path: String, value: String) {
        try {
            database.child(path).setValue(value).await()
        } catch (e: Exception) {
            println("Firebase Android: Error - ${e.message}")
        }
    }

    private fun userId(): String =
        auth.currentUser?.uid ?: "anonymous"

    actual suspend fun uploadTask(task: TaskModel, hash: String) {
        val uid = userId()
        val taskMap = mapOf(
            "id"          to task.id,
            "title"       to task.title,
            "description" to task.description,
            "isCompleted" to task.isCompleted,
            "dateMillis"  to (task.date?.let {
                it.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
            } ?: 0L),
            "timeMillisOfDay" to (task.time?.toMillisecondOfDay() ?: 0),
            "priority"    to task.priority,
            "categoryId"  to (task.categoryId ?: -1),
            "updatedAt"   to System.currentTimeMillis()
        )
        database.child("tasks").child(uid).child(task.id.toString())
            .setValue(taskMap).await()
        database.child("task_hashes").child(uid).child(task.id.toString())
            .setValue(hash).await()
    }

    actual suspend fun fetchRemoteHashes(): Map<Int, String> {
        return try {
            val uid = userId()
            val snapshot = database.child("task_hashes").child(uid).get().await()
            val result = mutableMapOf<Int, String>()
            snapshot.children.forEach { child ->
                val id = child.key?.toIntOrNull() ?: return@forEach
                val hash = child.getValue(String::class.java) ?: return@forEach
                result[id] = hash
            }
            result
        } catch (e: Exception) {
            emptyMap()
        }
    }

    actual suspend fun fetchRemoteTasks(): List<TaskModel> {
        return try {
            val uid = userId()
            val snapshot = database.child("tasks").child(uid).get().await()
            val tasks = mutableListOf<TaskModel>()
            snapshot.children.forEach { child ->
                val id = child.child("id").getValue(Int::class.java) ?: return@forEach
                val title = child.child("title").getValue(String::class.java) ?: return@forEach
                tasks.add(
                    TaskModel(
                        id          = id,
                        title       = title,
                        description = child.child("description").getValue(String::class.java) ?: "",
                        isCompleted = child.child("isCompleted").getValue(Boolean::class.java) ?: false,
                        priority    = child.child("priority").getValue(Int::class.java) ?: 1,
                        categoryId  = child.child("categoryId").getValue(Int::class.java)
                            ?.takeIf { it != -1 }
                    )
                )
            }
            tasks
        } catch (e: Exception) {
            emptyList()
        }
    }

    actual suspend fun deleteTask(taskId: Int) {
        try {
            val uid = userId()
            database.child("tasks").child(uid).child(taskId.toString()).removeValue().await()
            database.child("task_hashes").child(uid).child(taskId.toString()).removeValue().await()
        } catch (e: Exception) {
            println("Firebase Android: Error al eliminar - ${e.message}")
        }
    }
}
