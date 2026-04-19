package ucb.edu.bo.remoteconfig.presentation.state

data class RemoteConfigState(
    val welcomeMessage: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)