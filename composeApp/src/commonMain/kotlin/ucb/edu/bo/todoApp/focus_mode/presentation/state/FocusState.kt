package ucb.edu.bo.todoApp.focus_mode.presentation.state

import ucb.edu.bo.todoApp.focus_mode.domain.model.FocusSession

data class FocusState(
    val isRunning: Boolean = false,
    val elapsedSeconds: Int = 0,
    val selectedMinutes: Int = 25,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val weekSessions: List<FocusSession> = emptyList(),
    val isLoadingStats: Boolean = false,
    val weeklyAverageMinutes: Double = 0.0
)