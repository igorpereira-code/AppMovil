package ucb.edu.bo.events.domain.model

data class EventModel(
    val id: Int? = null,
    val type: String,
    val timestamp: Long
)