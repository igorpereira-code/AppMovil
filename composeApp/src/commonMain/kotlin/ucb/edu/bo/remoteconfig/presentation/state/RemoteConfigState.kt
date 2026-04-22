package ucb.edu.bo.remoteconfig.presentation.state

data class RemoteConfigState(
    val value: String = "",
    val isLoading: Boolean = false,
    val isFromCache: Boolean = false,
    val errorMessage: String? = null
)