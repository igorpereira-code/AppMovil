package ucb.edu.bo.todoApp.focus_mode.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import ucb.edu.bo.todoApp.focus_mode.domain.model.FocusSession
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

actual class FocusDataSource actual constructor() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: "anonymous"
    }

    private fun getCurrentWeekStart(): LocalDate {
        val today = LocalDate.now()
        return today.with(DayOfWeek.MONDAY)
    }

    actual suspend fun saveSession(session: FocusSession): Result<Unit> {
        return try {
            val userId = getCurrentUserId()
            val sessionMap = mapOf(
                "id" to session.id,
                "date" to session.date,
                "dayOfWeek" to session.dayOfWeek,
                "durationMinutes" to session.durationMinutes
            )
            database
                .child("focus_sessions")
                .child(userId)
                .child(session.id)
                .setValue(sessionMap)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual suspend fun getSessionsForCurrentWeek(): Result<List<FocusSession>> {
        return try {
            val userId = getCurrentUserId()
            val weekStart = getCurrentWeekStart()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val snapshot = database
                .child("focus_sessions")
                .child(userId)
                .get()
                .await()

            val sessions = mutableListOf<FocusSession>()
            snapshot.children.forEach { child ->
                val date = child.child("date").getValue(String::class.java) ?: return@forEach
                val sessionDate = LocalDate.parse(date, formatter)

                // Solo sesiones de la semana actual (lunes a domingo)
                if (!sessionDate.isBefore(weekStart) && !sessionDate.isAfter(weekStart.plusDays(6))) {
                    sessions.add(
                        FocusSession(
                            id = child.child("id").getValue(String::class.java) ?: "",
                            date = date,
                            dayOfWeek = child.child("dayOfWeek").getValue(String::class.java) ?: "",
                            durationMinutes = child.child("durationMinutes").getValue(Int::class.java) ?: 0
                        )
                    )
                }
            }
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}