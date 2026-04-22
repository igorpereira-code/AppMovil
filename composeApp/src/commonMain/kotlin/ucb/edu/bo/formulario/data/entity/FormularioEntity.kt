package ucb.edu.bo.formulario.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "formulario")
data class FormularioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val mensaje: String,
    val sincronizado: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)