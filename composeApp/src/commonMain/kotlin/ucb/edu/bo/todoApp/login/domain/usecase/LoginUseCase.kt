package ucb.edu.bo.todoApp.login.domain.usecase

import ucb.edu.bo.todoApp.login.domain.model.User
import ucb.edu.bo.todoApp.login.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}