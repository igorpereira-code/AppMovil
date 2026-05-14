package ucb.edu.bo.app.home.domain.model

data class Task(
    val title: String,
    val description: String,
    val time: String = "",
    val category: String = "",
    val priority: Int = 0,
    val isCompleted: Boolean = false
)
