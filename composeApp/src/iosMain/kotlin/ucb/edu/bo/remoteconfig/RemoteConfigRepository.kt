package ucb.edu.bo.remoteconfig

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class RemoteConfigRepository actual constructor() {
    actual fun observeMaintenance(): Flow<Boolean> = flowOf(false)
}