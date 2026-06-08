package ucb.edu.bo.todoApp.task.notification

actual class TaskNotificationScheduler actual constructor() {
    actual fun scheduleNotification(taskId: Int, title: String, timeInMillis: Long) {
        // Implementación nativa para iOS (UserNotifications framework)
        // Se puede agregar después si deciden compilar para iPhone
    }

    actual fun cancelNotification(taskId: Int) {
        // Lógica de cancelación en iOS
    }
}

actual fun convertDateTimeToMillis(date: String, time: String): Long {
    // Stub temporal para iOS
    return 0L
}