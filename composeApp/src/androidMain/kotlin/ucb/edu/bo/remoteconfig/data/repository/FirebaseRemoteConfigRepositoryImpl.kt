package ucb.edu.bo.remoteconfig.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.ConfigUpdateListenerRegistration
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.remoteconfig.domain.repository.IRemoteConfigRepository

class FirebaseRemoteConfigRepositoryImpl : IRemoteConfigRepository {

    private val remoteConfig = Firebase.remoteConfig
    private val _isMaintenanceMode = MutableStateFlow(false)
    private val MAINTENANCE_KEY = "is_under_maintenance"

    private var listenerRegistration: ConfigUpdateListenerRegistration? = null

    init {
        println("🔥 FIREBASE: 1. Arrancando sistema híbrido...")

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(MAINTENANCE_KEY to false))

        // Conexión inicial
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            _isMaintenanceMode.value = remoteConfig.getBoolean(MAINTENANCE_KEY)
            println("🔥 FIREBASE: 2. Valor inicial cargado: ${_isMaintenanceMode.value}")

            // Iniciamos ambos sistemas por seguridad
            startRealTimeListener()
            startBackupPolling() // <-- EL SALVADOR
        }
    }

    private fun startRealTimeListener() {
        listenerRegistration = remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(MAINTENANCE_KEY)) {
                    remoteConfig.activate().addOnCompleteListener {
                        _isMaintenanceMode.value = remoteConfig.getBoolean(MAINTENANCE_KEY)
                        println("🔥 FIREBASE: ¡Cambio detectado por Listener Oficial!")
                    }
                }
            }
            override fun onError(error: FirebaseRemoteConfigException) {
                println("🔥 FIREBASE: Error en Listener oficial, dependemos del Polling.")
            }
        })
    }

    // --- PLAN B: Polling Activo ---
    // Pregunta a Firebase cada 5 segundos. Como intervalInSeconds es 0, siempre trae el último valor.
    private fun startBackupPolling() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(5000) // Espera 5 segundos
                remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val newValue = remoteConfig.getBoolean(MAINTENANCE_KEY)
                        if (_isMaintenanceMode.value != newValue) {
                            println("🔥 FIREBASE: ¡Cambio detectado por Plan B (Polling)!")
                            _isMaintenanceMode.value = newValue
                        }
                    }
                }
            }
        }
    }

    override fun getMaintenanceMode(): StateFlow<Boolean> = _isMaintenanceMode.asStateFlow()
}