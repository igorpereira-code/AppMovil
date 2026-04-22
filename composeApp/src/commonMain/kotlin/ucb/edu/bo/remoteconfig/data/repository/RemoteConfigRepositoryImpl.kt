package ucb.edu.bo.remoteconfig.data.repository

import ucb.edu.bo.firebase.RemoteConfigManager
//import ucb.edu.bo.firebase.data.datasource.RemoteConfigManager
import ucb.edu.bo.remoteconfig.data.dao.RemoteConfigDao
import ucb.edu.bo.remoteconfig.data.entity.RemoteConfigEntity
import ucb.edu.bo.remoteconfig.domain.model.RemoteConfigModel
import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository

class RemoteConfigRepositoryImpl(
    private val remoteConfigManager: RemoteConfigManager,
    private val remoteConfigDao: RemoteConfigDao
) : RemoteConfigRepository {

    override suspend fun fetchAndActivate(): Boolean {
        return remoteConfigManager.fetchAndActivate()
    }

    override suspend fun getRemoteValue(key: String): String {
        return remoteConfigManager.getString(key)
    }

    override suspend fun getCachedValue(key: String): RemoteConfigModel? {
        return remoteConfigDao.getByKey(key)?.let {
            RemoteConfigModel(
                key = it.key,
                value = it.value,
                updatedAt = it.updatedAt
            )
        }
    }

    override suspend fun saveToCache(key: String, value: String) {
        remoteConfigDao.insertOrUpdate(
            RemoteConfigEntity(key = key, value = value)
        )
    }
}