package ucb.edu.bo.formulario.presentation.state

data class FormularioState(
    val nombre: String = "",
    val mensaje: String = "",
    val isLoading: Boolean = false,
    val isSynced: Boolean = false,
    val lastSaved: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null
)