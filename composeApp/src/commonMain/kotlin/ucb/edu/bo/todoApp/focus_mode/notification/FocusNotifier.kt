package ucb.edu.bo.todoApp.focus_mode.notification

expect class FocusNotifier() {
    fun notify(minutes: Int)
    // NUEVAS FUNCIONES PARA EL CRONÓMETRO
    fun startTimerNotification(timeText: String)
    fun updateTimerNotification(timeText: String)
    fun stopTimerNotification()
}