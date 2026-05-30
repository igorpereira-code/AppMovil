package ucb.edu.bo.todoApp.settings.domain.repository

interface CalendarAuthRepository {
    // Caso de uso para descargar y procesar los eventos de la cuenta vinculada
    suspend fun importPrimaryCalendarEvents(): Result<Unit>
}