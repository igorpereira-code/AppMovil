package ucb.edu.bo.formulario.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ucb.edu.bo.formulario.data.entity.FormularioEntity

@Dao
interface FormularioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: FormularioEntity)

    @Query("SELECT * FROM formulario ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLatest(): FormularioEntity?

    @Query("UPDATE formulario SET sincronizado = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)
}