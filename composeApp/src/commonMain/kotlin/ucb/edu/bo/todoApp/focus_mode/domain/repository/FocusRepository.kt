package ucb.edu.bo.todoApp.focus_mode.domain.repository

import ucb.edu.bo.todoApp.focus_mode.domain.model.FocusSession

interface FocusRepository {
    suspend fun saveSession(session: FocusSession): Result<Unit>
    suspend fun getSessionsForCurrentWeek(): Result<List<FocusSession>>
}