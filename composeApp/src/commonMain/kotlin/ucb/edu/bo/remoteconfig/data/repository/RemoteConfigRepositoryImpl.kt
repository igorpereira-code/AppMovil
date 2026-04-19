package ucb.edu.bo.remoteconfig.data.repository

import ucb.edu.bo.firebase.RemoteConfigManager
import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository

class RemoteConfigRepositoryImpl(
    private val remoteConfigManager: RemoteConfigManager
) : RemoteConfigRepository {

    override suspend fun fetchAndActivate(): Boolean {
        return remoteConfigManager.fetchAndActivate()
    }

    override fun getString(key: String): String {
        return remoteConfigManager.getString(key)
    }
}