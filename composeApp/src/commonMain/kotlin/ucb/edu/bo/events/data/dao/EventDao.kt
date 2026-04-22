package ucb.edu.bo.events.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ucb.edu.bo.events.data.entity.EventEntity

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: EventEntity)

    @Query("SELECT * FROM app_events WHERE is_synced = 0")
    suspend fun getUnsyncedEvents(): List<EventEntity>

    @Query("UPDATE app_events SET is_synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)
}