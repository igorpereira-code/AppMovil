package ucb.edu.bo.todoApp.profile.data.repository

import ucb.edu.bo.todoApp.profile.data.datasource.ProfileDataSource
import ucb.edu.bo.todoApp.profile.domain.model.UserProfile
import ucb.edu.bo.todoApp.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileDataSource: ProfileDataSource
) : ProfileRepository {

    override suspend fun getProfile(): Result<UserProfile> {
        return profileDataSource.getProfile()
    }

    override suspend fun updateName(newName: String): Result<Unit> {
        return profileDataSource.updateName(newName)
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String): Result<Unit> {
        return profileDataSource.updatePassword(oldPassword, newPassword)
    }

    override suspend fun logout(): Result<Unit> {
        return profileDataSource.logout()
    }
}