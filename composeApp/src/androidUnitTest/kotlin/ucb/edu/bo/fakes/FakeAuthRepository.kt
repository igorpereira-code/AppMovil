package ucb.edu.bo.fakes

import ucb.edu.bo.todoApp.login.domain.model.User
import ucb.edu.bo.todoApp.login.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {

    var loginResult: Result<User> = Result.success(
        User(
            uid = "1",
            username = "Igor",
            email = "igor@test.com"
        )
    )

    var registerResult: Result<User> = Result.success(
        User(
            uid = "1",
            username = "Igor",
            email = "igor@test.com"
        )
    )

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return loginResult
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<User> {
        return registerResult
    }

    override suspend fun changePassword(
        newPassword: String
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateName(
        newName: String
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun logout(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun getCurrentUserName(): String {
        return "Igor"
    }
}