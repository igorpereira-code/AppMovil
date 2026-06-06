package ucb.edu.bo.config.domain.repository

import ucb.edu.bo.config.domain.model.ConfigModel

interface ConfigRepository {
    // Sincroniza con Firebase y luego devuelve lo que hay en Room
    suspend fun syncAndGetConfig(key: String): ConfigModel?
}