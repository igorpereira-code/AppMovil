package ucb.edu.bo.firebase

expect class RemoteConfigManager() {
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String): String
}