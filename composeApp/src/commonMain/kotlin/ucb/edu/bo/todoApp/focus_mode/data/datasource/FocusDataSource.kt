package ucb.edu.bo.todoApp.focus_mode.data.datasource

import ucb.edu.bo.todoApp.focus_mode.domain.model.FocusSession

expect class FocusDataSource() {
    suspend fun saveSession(session: FocusSession): Result<Unit>
    suspend fun getSessionsForCurrentWeek(): Result<List<FocusSession>>
}