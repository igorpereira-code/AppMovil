package ucb.edu.bo.events.domain.usecase

import ucb.edu.bo.events.domain.repository.EventRepository

class LogAndSyncAppEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventType: String) {
        // 1. Guarda en Room
        repository.logEvent(eventType)
        // 2. Intenta subir a Firebase Realtime Database
        repository.syncPendingEvents()
    }
}