package ucb.edu.bo.formulario.domain.model

data class FormularioModel(
    val id: Int = 0,
    val nombre: String,
    val mensaje: String,
    val sincronizado: Boolean = false,
    val updatedAt: Long = 0L
)