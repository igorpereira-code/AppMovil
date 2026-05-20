package ucb.edu.bo.todoApp.focus_mode.domain.usecase

import ucb.edu.bo.todoApp.focus_mode.domain.model.FocusSession
import ucb.edu.bo.todoApp.focus_mode.domain.repository.FocusRepository

class SaveFocusSessionUseCase(
    private val focusRepository: FocusRepository
) {
    suspend operator fun invoke(session: FocusSession): Result<Unit> {
        return focusRepository.saveSession(session)
    }
}