package ucb.edu.bo.remoteconfig.domain.repository

import ucb.edu.bo.remoteconfig.domain.model.RemoteConfigModel

interface RemoteConfigRepository {
    suspend fun fetchAndActivate(): Boolean
    suspend fun getRemoteValue(key: String): String
    suspend fun getCachedValue(key: String): RemoteConfigModel?
    suspend fun saveToCache(key: String, value: String)
}