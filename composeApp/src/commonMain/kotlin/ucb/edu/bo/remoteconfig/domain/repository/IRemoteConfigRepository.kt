package ucb.edu.bo.remoteconfig.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface IRemoteConfigRepository {
    // Devuelve true si la app está en mantenimiento, false si funciona normal
    fun getMaintenanceMode(): StateFlow<Boolean>
}