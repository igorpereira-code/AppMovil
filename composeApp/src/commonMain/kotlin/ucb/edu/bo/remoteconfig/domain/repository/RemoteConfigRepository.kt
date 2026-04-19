package ucb.edu.bo.remoteconfig.domain.repository

interface RemoteConfigRepository {
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String): String
}