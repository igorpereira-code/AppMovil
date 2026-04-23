package ucb.edu.bo.events.domain.repository

import ucb.edu.bo.events.domain.model.EventModel

interface EventRepository {
    suspend fun save(model: EventModel)
    suspend fun getList(): List<EventModel>
    suspend fun clear()
}