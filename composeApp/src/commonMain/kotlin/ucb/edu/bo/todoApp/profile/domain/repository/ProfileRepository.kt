package ucb.edu.bo.todoApp.profile.domain.repository

import ucb.edu.bo.todoApp.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateName(newName: String): Result<Unit>
    suspend fun updatePassword(oldPassword: String, newPassword: String): Result<Unit>
    suspend fun logout(): Result<Unit>
}