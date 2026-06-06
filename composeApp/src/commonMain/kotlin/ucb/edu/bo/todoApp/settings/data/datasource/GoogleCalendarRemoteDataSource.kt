package ucb.edu.bo.todoApp.settings.data.datasource

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import ucb.edu.bo.todoApp.settings.data.dto.GoogleCalendarResponseDto

class GoogleCalendarRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun fetchPrimaryCalendarEvents(accessToken: String): GoogleCalendarResponseDto {
        // Consumimos el endpoint oficial de la API de Google Calendar
        return httpClient.get("https://www.googleapis.com/calendar/v3/calendars/primary/events") {
            header("Authorization", "Bearer $accessToken")
        }.body()
    }
}