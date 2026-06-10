package ucb.edu.bo.todoApp.login.domain.repository

import ucb.edu.bo.todoApp.login.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(username: String, email: String, password: String): Result<User>

    suspend fun changePassword(newPassword: String): Result<Unit>
    suspend fun updateName(newName: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    fun getCurrentUserName(): String
    fun getCurrentUserId(): String // NUEVO: Para vincular datos al usuario
}
