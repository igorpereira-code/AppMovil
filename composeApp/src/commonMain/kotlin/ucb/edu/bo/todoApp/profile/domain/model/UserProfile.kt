package ucb.edu.bo.todoApp.profile.domain.model

data class UserProfile(
    val name: String,
    val tasksLeft: Int,
    val tasksDone: Int,
    val profileImageUrl: String? = null
)