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
                // Sube a Realtime Database en la ruta "eventos_app/{timestamp}"
                val path = "isabel/${event.timestamp}"
                val value = "Evento: ${event.eventType}"

                firebaseManager.saveData(path, value)

                // Si tuvo éxito, lo marca como sincronizado en Room
                eventDao.markAsSynced(event.id)
            } catch (e: Exception) {
                // Si falla (ej. sin internet), se queda como is_synced = false para el futuro
                println("Fallo sincronización de evento: ${e.message}")
            }
        }
    }
}