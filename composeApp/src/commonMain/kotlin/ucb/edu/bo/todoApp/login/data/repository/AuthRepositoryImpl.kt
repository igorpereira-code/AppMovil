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

    override suspend fun changePassword(newPassword: String): Result<Unit> {
        return authDataSource.changePassword(newPassword)
    }

    override suspend fun updateName(newName: String): Result<Unit> {
        return authDataSource.updateName(newName)
    }

    override suspend fun logout(): Result<Unit> {
        return authDataSource.logout()
    }

    override fun getCurrentUserName(): String {
        return authDataSource.getCurrentUserName()
    }

    override fun getCurrentUserId(): String {
        return authDataSource.getCurrentUserId()
    }
}
