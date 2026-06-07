package ucb.edu.bo.todoApp.task.notification

// El "molde" que usará tu ViewModel en commonMain
expect class TaskNotificationScheduler() {
    fun scheduleNotification(taskId: Int, title: String, timeInMillis: Long)
    fun cancelNotification(taskId: Int)
}

// Función auxiliar para calcular la hora exacta de la alarma
expect fun convertDateTimeToMillis(date: String, time: String): Long