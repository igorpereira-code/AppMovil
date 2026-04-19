package ucb.edu.bo.remoteconfig

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

actual class RemoteConfigRepository actual constructor() {

    companion object {
        private const val POLL_INTERVAL_MS = 10_000L
    }

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        // OJO: Mantengo la llave "mantainence" como en tu código original
        remoteConfig.setDefaultsAsync(mapOf("mantainence" to false))
    }

    actual fun observeMaintenance(): Flow<Boolean> = callbackFlow {

        suspend fun fetchAndEmit() {
            try {
                remoteConfig.fetchAndActivate().await()
            } catch (_: Exception) { }
            trySend(remoteConfig.getBoolean("mantainence"))
        }

        fetchAndEmit()

        val listener = object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate().addOnSuccessListener {
                    trySend(remoteConfig.getBoolean("mantainence"))
                }
            }
            override fun onError(error: FirebaseRemoteConfigException) { }
        }
        val registration = remoteConfig.addOnConfigUpdateListener(listener)

        val pollingJob = launch {
            while (isActive) {
                delay(POLL_INTERVAL_MS)
                fetchAndEmit()
            }
        }

        awaitClose {
            registration.remove()
            pollingJob.cancel()
        }
    }
}