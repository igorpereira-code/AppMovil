package ucb.edu.bo.todoApp.profile.presentation.state

data class ProfileUIState(
    val userName: String = "",
    val tasksLeft: Int = 0,
    val tasksDone: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showNameDialog: Boolean = false,
    val showPasswordDialog: Boolean = false,
    val showImageBottomSheet: Boolean = false
)