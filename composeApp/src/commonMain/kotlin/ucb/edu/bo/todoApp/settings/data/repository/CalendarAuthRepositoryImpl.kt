package ucb.edu.bo.todoApp.settings.data.repository

import ucb.edu.bo.todoApp.settings.data.datasource.GoogleCalendarRemoteDataSource
import ucb.edu.bo.todoApp.settings.domain.repository.CalendarAuthRepository
import ucb.edu.bo.todoApp.task.data.dao.TaskDao
import ucb.edu.bo.todoApp.task.data.entity.TaskEntity
import kotlinx.datetime.Instant

class CalendarAuthRepositoryImpl(
    private val remoteDataSource: GoogleCalendarRemoteDataSource,
    private val taskDao: TaskDao
) : CalendarAuthRepository {

    override suspend fun importPrimaryCalendarEvents(): Result<Unit> {
        return try {
            // NOTA TÉCNICA: Aquí deberías obtener el token de acceso OAuth2 activo del usuario.
            // Para la arquitectura base, asumimos que se recupera o se inyecta un token válido.
            val mockToken = "YA_SOPORTA_OAUTH_TOKEN_REAL"

            // 1. Descargar eventos desde Google Calendar API
            val response = remoteDataSource.fetchPrimaryCalendarEvents(mockToken)

            // 2. Mapear e insertar cada evento en tu base de datos local Room
            response.items.forEach { googleEvent ->
                val title = googleEvent.summary ?: "Google Calendar Event"
                val description = googleEvent.description ?: ""

                // Procesar la fecha ISO 8601 de Google a milisegundos de época
                val dateIso = googleEvent.start?.dateTime ?: googleEvent.start?.date
                val createdAtMillis = dateIso?.let {
                    try { Instant.parse(it).toEpochMilliseconds() } catch(e: Exception) { null }
                } ?: kotlinx.datetime.Clock.System.now().toEpochMilliseconds()

                val taskEntity = TaskEntity(
                    title = title,
                    description = description,
                    isCompleted = false,
                    createdAt = createdAtMillis,
                    isSynced = false // Se marcará para que tu TaskSyncWorker lo suba también a Firebase si es necesario
                )

                // Guardar en la base de datos única (dollar_db.db)
                taskDao.insert(taskEntity)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}