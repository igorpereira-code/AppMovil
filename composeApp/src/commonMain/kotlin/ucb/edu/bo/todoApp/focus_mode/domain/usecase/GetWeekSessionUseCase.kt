package ucb.edu.bo.todoApp.focus_mode.domain.usecase

import ucb.edu.bo.todoApp.focus_mode.domain.model.FocusSession
import ucb.edu.bo.todoApp.focus_mode.domain.repository.FocusRepository

class GetWeekSessionsUseCase(
    private val focusRepository: FocusRepository
) {
    suspend operator fun invoke(): Result<List<FocusSession>> {
        return focusRepository.getSessionsForCurrentWeek()
    }
}