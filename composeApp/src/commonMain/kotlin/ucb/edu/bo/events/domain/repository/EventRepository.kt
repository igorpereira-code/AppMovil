package ucb.edu.bo.events.domain.repository

interface EventRepository {
    suspend fun logEvent(type: String)
    suspend fun syncPendingEvents()


}