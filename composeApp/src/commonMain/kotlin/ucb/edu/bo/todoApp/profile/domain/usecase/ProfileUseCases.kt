package ucb.edu.bo.todoApp.profile.domain.usecase

import ucb.edu.bo.todoApp.profile.domain.model.UserProfile
import ucb.edu.bo.todoApp.profile.domain.repository.ProfileRepository

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

class LogoutUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}