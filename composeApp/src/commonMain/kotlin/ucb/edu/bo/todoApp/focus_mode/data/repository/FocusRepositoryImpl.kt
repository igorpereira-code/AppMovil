package ucb.edu.bo.todoApp.focus_mode.data.repository

import ucb.edu.bo.todoApp.focus_mode.data.datasource.FocusDataSource
import ucb.edu.bo.todoApp.focus_mode.domain.model.FocusSession
import ucb.edu.bo.todoApp.focus_mode.domain.repository.FocusRepository

class FocusRepositoryImpl(
    private val focusDataSource: FocusDataSource
) : FocusRepository {

    override suspend fun saveSession(session: FocusSession): Result<Unit> {
        return focusDataSource.saveSession(session)
    }

    override suspend fun getSessionsForCurrentWeek(): Result<List<FocusSession>> {
        return focusDataSource.getSessionsForCurrentWeek()
    }
}