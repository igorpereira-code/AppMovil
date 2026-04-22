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
            // 1. Intenta descargar de Remote Config
            val success = remoteConfigManager.fetchAndActivate()

            // 2. Obtenemos el valor más reciente de Firebase
            val remoteValue = remoteConfigManager.getString(key)

            // 3. Si existe, lo guardamos en Room para el caché
            if (remoteValue.isNotEmpty()) {
                localDao.insertConfig(ConfigEntity(key = key, value = remoteValue))
            }
        } catch (e: Exception) {
            // Falla por falta de internet o error de Firebase.
            // No hacemos throw, dejamos que pase al paso 4 para leer el caché.
            println("Error sincronizando Firebase: ${e.message}")
        }

        // 4. Siempre devolvemos la última versión guardada en Room
        return localDao.getConfig(key)?.toModel()
    }
}