package ucb.edu.bo.remoteconfig.domain.usecase

import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository

class FetchRemoteConfigUseCase(
    private val repository: RemoteConfigRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.fetchAndActivate()
    }
}