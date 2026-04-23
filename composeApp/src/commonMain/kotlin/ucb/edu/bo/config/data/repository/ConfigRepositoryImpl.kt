package ucb.edu.bo.config.data.repository

import ucb.edu.bo.config.data.dao.ConfigDao
import ucb.edu.bo.config.data.entity.ConfigEntity
import ucb.edu.bo.config.data.mapper.toModel
import ucb.edu.bo.config.domain.model.ConfigModel
import ucb.edu.bo.config.domain.repository.ConfigRepository
import ucb.edu.bo.firebase.RemoteConfigManager

class ConfigRepositoryImpl(
    private val remoteConfigManager: RemoteConfigManager,
    private val localDao: ConfigDao
) : ConfigRepository {

    override suspend fun syncAndGetConfig(key: String): ConfigModel? {
        try {
            val success = remoteConfigManager.fetchAndActivate()

            val remoteValue = remoteConfigManager.getString(key)

            if (remoteValue.isNotEmpty()) {
                localDao.insertConfig(ConfigEntity(key = key, value = remoteValue))
            }
        } catch (e: Exception) {

            println("Error sincronizando Firebase: ${e.message}")
        }


        return localDao.getConfig(key)?.toModel()
    }
}