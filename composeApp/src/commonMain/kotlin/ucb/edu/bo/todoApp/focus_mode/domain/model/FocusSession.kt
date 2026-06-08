package ucb.edu.bo.todoApp.focus_mode.domain.model
data class FocusSession(
    val id: String = "",
    val date: String = "",        // formato "2025-05-19"
    val dayOfWeek: String = "",   // "MON", "TUE", "WED", etc.
    val durationMinutes: Int = 0
)