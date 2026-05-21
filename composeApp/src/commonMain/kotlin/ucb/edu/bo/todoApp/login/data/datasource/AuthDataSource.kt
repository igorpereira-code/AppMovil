package ucb.edu.bo.todoApp.login.data.datasource

import ucb.edu.bo.todoApp.login.domain.model.User

expect class AuthDataSource() {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(username: String, email: String, password: String): Result<User>
}