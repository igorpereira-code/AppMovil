package ucb.edu.bo.events.domain.usecase

import ucb.edu.bo.events.domain.model.EventModel
import ucb.edu.bo.events.domain.repository.EventRepository

class CreateEventUseCase(private val repository: EventRepository) {
    suspend operator fun invoke(model: EventModel) {
        repository.save(model)
    }
}