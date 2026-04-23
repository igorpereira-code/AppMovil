package ucb.edu.bo.events.domain.usecase

import ucb.edu.bo.events.domain.model.EventModel
import ucb.edu.bo.events.domain.repository.EventRepository

class GetEventListUseCase(private val repository: EventRepository) {
    suspend operator fun invoke(): List<EventModel> {
        return repository.getList()
    }
}