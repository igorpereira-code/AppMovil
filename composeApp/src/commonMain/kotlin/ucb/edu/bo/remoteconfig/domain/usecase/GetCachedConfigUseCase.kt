package ucb.edu.bo.remoteconfig.domain.usecase

import ucb.edu.bo.remoteconfig.domain.model.RemoteConfigModel
import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository

class GetCachedConfigUseCase(
    private val repository: RemoteConfigRepository
) {
    suspend operator fun invoke(key: String): RemoteConfigModel? {
        return repository.getCachedValue(key)
    }
}