package ucb.edu.bo.todoApp.profile.domain.usecase

import ucb.edu.bo.todoApp.login.domain.repository.AuthRepository
import ucb.edu.bo.todoApp.profile.domain.model.UserProfile
import ucb.edu.bo.todoApp.profile.domain.repository.ProfileRepository
import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getProfile()
    }
}

class UpdateNameUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(newName: String): Result<Unit> {
        return repository.updateName(newName)
    }
}

class UpdatePasswordUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(oldPassword: String, newPassword: String): Result<Unit> {
        return repository.updatePassword(oldPassword, newPassword)
    }
}

class LogoutUseCase(
    private val profileRepository: ProfileRepository,
    private val taskRepository: TaskRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            val userId = authRepository.getCurrentUserId()
            // Limpiamos los datos locales del usuario antes de cerrar sesión
            if (userId.isNotEmpty()) {
                taskRepository.clearLocalData(userId)
            }
            profileRepository.logout()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
