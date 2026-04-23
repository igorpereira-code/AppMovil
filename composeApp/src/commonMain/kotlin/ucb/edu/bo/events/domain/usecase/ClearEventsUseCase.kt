package ucb.edu.bo.events.domain.usecase

import ucb.edu.bo.events.domain.repository.EventRepository

class ClearEventsUseCase(private val repository: EventRepository) {
    suspend operator fun invoke() {
        repository.clear()
    }
}