package ucb.edu.bo.todoApp.login.domain.usecase

import ucb.edu.bo.todoApp.login.domain.model.User
import ucb.edu.bo.todoApp.login.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String): Result<User> {
        return authRepository.register(username, email, password)
    }
}