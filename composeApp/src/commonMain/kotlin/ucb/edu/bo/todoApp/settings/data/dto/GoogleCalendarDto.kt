package ucb.edu.bo.todoApp.settings.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleCalendarResponseDto(
    val items: List<GoogleEventDto> = emptyList()
)

@Serializable
data class GoogleEventDto(
    val id: String,
    val summary: String? = null, // Título del evento
    val description: String? = null,
    val start: EventDateTimeDto? = null,
    val priority: Int = 1
)

@Serializable
data class EventDateTimeDto(
    val dateTime: String? = null, // Formato ISO: "2026-05-29T15:00:00Z"
    val date: String? = null      // Para eventos de todo el día: "2026-05-29"
)