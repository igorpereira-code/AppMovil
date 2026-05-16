package ucb.edu.bo.todoApp.login.domain.repository

import ucb.edu.bo.todoApp.login.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(username: String, email: String, password: String): Result<User>
}