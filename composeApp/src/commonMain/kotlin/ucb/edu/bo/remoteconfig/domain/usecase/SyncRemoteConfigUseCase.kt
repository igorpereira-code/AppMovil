package ucb.edu.bo.remoteconfig.domain.usecase

import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository

class SyncRemoteConfigUseCase(
    private val repository: RemoteConfigRepository
) {
    suspend operator fun invoke(key: String): Result<String> {
        return try {
            repository.fetchAndActivate()
            val value = repository.getRemoteValue(key)
            repository.saveToCache(key, value)
            Result.success(value)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}