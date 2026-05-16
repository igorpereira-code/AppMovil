package ucb.edu.bo.todoApp.login.data.repository

import ucb.edu.bo.todoApp.login.data.datasource.AuthDataSource
import ucb.edu.bo.todoApp.login.domain.model.User
import ucb.edu.bo.todoApp.login.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return authDataSource.login(email, password)
    }

    override suspend fun register(username: String, email: String, password: String): Result<User> {
        return authDataSource.register(username, email, password)
    }
}