package ucb.edu.bo.events.data.datasource

import ucb.edu.bo.events.data.entity.EventEntity

interface EventLocalDataSource {
    suspend fun save(entity: EventEntity)
    suspend fun list(): List<EventEntity>
    suspend fun clear()
}