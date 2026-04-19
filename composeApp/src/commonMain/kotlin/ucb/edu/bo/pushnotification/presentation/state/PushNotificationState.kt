package ucb.edu.bo.pushnotification.presentation.state

data class PushNotificationState(
    val token: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)