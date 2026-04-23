package ucb.edu.bo.events.data.repository

import ucb.edu.bo.events.data.dao.EventDao
import ucb.edu.bo.events.data.entity.EventEntity
import ucb.edu.bo.events.domain.repository.EventRepository
import ucb.edu.bo.firebase.FirebaseManager
import ucb.edu.bo.utils.getCurrentTimeMillis

class EventRepositoryImpl(
    private val eventDao: EventDao
) : EventRepository {

    private val firebaseManager = FirebaseManager()

    override suspend fun logEvent(type: String) {
        val event = EventEntity(
            eventType = type,
            timestamp = getCurrentTimeMillis()
        )
        eventDao.insertEvent(event)
    }

    override suspend fun syncPendingEvents() {
        val unsynced = eventDao.getUnsyncedEvents()
        for (event in unsynced) {
            try {
                val path = "isabel/${event.timestamp}"
                val value = "Evento: ${event.eventType}"

                firebaseManager.saveData(path, value)

                eventDao.markAsSynced(event.id)
            } catch (e: Exception) {
                println("Fallo sincronización de evento: ${e.message}")
            }
        }
    }
}