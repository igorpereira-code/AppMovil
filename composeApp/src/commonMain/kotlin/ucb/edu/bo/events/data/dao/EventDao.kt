package ucb.edu.bo.events.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ucb.edu.bo.events.data.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    suspend fun getList(): List<EventEntity>

    @Insert
    suspend fun insert(event: EventEntity)

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}