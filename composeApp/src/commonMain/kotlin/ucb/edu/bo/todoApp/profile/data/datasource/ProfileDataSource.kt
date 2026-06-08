package ucb.edu.bo.todoApp.profile.data.datasource

import ucb.edu.bo.todoApp.profile.domain.model.UserProfile
import kotlinx.coroutines.delay

class ProfileDataSource {
    // Datos simulados (mock) temporalmente
    private var currentName = "Martha Hays"

    suspend fun getProfile(): Result<UserProfile> {
        delay(500) // Simulamos tiempo de carga de red
        return Result.success(
            UserProfile(
                name = currentName,
                tasksLeft = 10, // Aquí luego conectarás con tus verdaderas tareas
                tasksDone = 5
            )
        )
    }

    suspend fun updateName(newName: String): Result<Unit> {
        delay(500)
        currentName = newName
        return Result.success(Unit)
    }

    suspend fun updatePassword(oldPassword: String, newPassword: String): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }

    suspend fun logout(): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }
}