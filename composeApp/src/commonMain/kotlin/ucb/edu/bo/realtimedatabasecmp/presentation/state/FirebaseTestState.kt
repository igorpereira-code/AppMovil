package ucb.edu.bo.realtimedatabasecmp.presentation.state

data class FirebaseTestState(
    val inputText: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)