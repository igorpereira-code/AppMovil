package ucb.edu.bo.events.domain.usecase

import ucb.edu.bo.events.domain.repository.EventRepository

class LogAndSyncAppEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventType: String) {
        repository.logEvent(eventType)
        repository.syncPendingEvents()
    }
}