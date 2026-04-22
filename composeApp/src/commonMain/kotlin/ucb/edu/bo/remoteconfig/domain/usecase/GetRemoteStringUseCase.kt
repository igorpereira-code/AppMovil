package ucb.edu.bo.remoteconfig.domain.usecase

import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository

class GetRemoteStringUseCase(
    private val repository: RemoteConfigRepository
) {
    suspend operator fun invoke(key: String): String {
        return repository.getRemoteValue(key)
    }
}