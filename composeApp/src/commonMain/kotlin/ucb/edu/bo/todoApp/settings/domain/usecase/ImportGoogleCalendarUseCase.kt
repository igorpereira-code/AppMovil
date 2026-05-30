package ucb.edu.bo.todoApp.settings.domain.usecase

import ucb.edu.bo.todoApp.settings.domain.repository.CalendarAuthRepository

class ImportGoogleCalendarUseCase(
    private val repository: CalendarAuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.importPrimaryCalendarEvents()
    }
}