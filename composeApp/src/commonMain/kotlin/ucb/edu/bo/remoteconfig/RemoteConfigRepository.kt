package ucb.edu.bo.remoteconfig

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz expect para Firebase Remote Config adaptada a ucb.edu.bo.
 */
expect class RemoteConfigRepository() {
    fun observeMaintenance(): Flow<Boolean>
}