package ucb.edu.bo.fakes

import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository

class FakeTaskRepository : TaskRepository {
    var shouldFail = false
    var failureMessage = "Task operation failed"
    private val tasks = mutableListOf<TaskModel>()
    private var idCounter = 1

    val tasksSnapshot: List<TaskModel> get() = tasks.toList()

    fun setTasks(list: List<TaskModel>) {
        tasks.clear()
        tasks.addAll(list)
    }

    override suspend fun getAll(): List<TaskModel> {
        if (shouldFail) throw Exception(failureMessage)
        return tasks.toList()
    }

    override suspend fun getById(taskId: Int): TaskModel? {
        if (shouldFail) throw Exception(failureMessage)
        return tasks.find { it.id == taskId }
    }

    override suspend fun save(task: TaskModel) {
        if (shouldFail) throw Exception(failureMessage)
        val withId = if (task.id == 0) task.copy(id = idCounter++) else task
        tasks.add(withId)
    }

    override suspend fun update(task: TaskModel) {
        if (shouldFail) throw Exception(failureMessage)
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) tasks[index] = task
    }

    override suspend fun delete(taskId: Int) {
        if (shouldFail) throw Exception(failureMessage)
        tasks.removeAll { it.id == taskId }
    }

    override suspend fun toggleComplete(taskId: Int, isCompleted: Boolean) {
        if (shouldFail) throw Exception(failureMessage)
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index >= 0) tasks[index] = tasks[index].copy(isCompleted = isCompleted)
    }

    override suspend fun markAsSynced(taskId: Int) {
        if (shouldFail) throw Exception(failureMessage)
    }
}