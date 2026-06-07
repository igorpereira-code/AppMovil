package ucb.edu.bo.config.presentation.state

data class SyncConfigState(
    val isLoading: Boolean = true,
    val configValue: String = "",
    val errorMessage: String? = null
)