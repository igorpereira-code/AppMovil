package ucb.edu.bo.config.domain.repository

import ucb.edu.bo.config.domain.model.ConfigModel

interface ConfigRepository {
    suspend fun syncAndGetConfig(key: String): ConfigModel?
}