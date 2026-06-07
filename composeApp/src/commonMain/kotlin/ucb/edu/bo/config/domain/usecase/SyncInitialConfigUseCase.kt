package ucb.edu.bo.config.domain.usecase

import ucb.edu.bo.config.domain.model.ConfigModel
import ucb.edu.bo.config.domain.repository.ConfigRepository

class SyncInitialConfigUseCase(
    private val repository: ConfigRepository
) {
    suspend operator fun invoke(key: String): ConfigModel? {
        return repository.syncAndGetConfig(key)
    }
}