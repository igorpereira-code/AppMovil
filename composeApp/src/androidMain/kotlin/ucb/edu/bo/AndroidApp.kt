package ucb.edu.bo

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ucb.edu.bo.di.getModules
import ucb.edu.bo.events.domain.model.EventModel
import ucb.edu.bo.events.domain.usecase.CreateEventUseCase
import ucb.edu.bo.workmanager.InitialSyncWorker
import ucb.edu.bo.workmanager.LogScheduler
import ucb.edu.bo.workmanager.LogUploadWorker

class AndroidApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApp)
            modules(getModules())
        }

        val createEventUseCase = getKoin().get<CreateEventUseCase>()

        // --- REGISTRAR APERTURA ---
        MainScope().launch {
            createEventUseCase(EventModel(type = "OPEN", timestamp = System.currentTimeMillis()))
        }

        // --- LÓGICA PARA DETECTAR EL CIERRE (CLOSED) ---
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var activityCount = 0

            override fun onActivityStarted(activity: Activity) {
                activityCount++
            }

            override fun onActivityStopped(activity: Activity) {
                activityCount--
                if (activityCount == 0) {
                    // La app se fue al fondo
                    MainScope().launch {
                        // 1. Guardamos el evento CLOSED en Room
                        createEventUseCase(EventModel(type = "CLOSED", timestamp = System.currentTimeMillis()))

                        // 2. FORZAMOS LA SUBIDA INMEDIATA A FIREBASE
                        // Esto hace que el "CLOSED" aparezca rápido en la consola
                        val syncRequest = OneTimeWorkRequest.Builder(LogUploadWorker::class.java).build()
                        WorkManager.getInstance(this@AndroidApp).enqueue(syncRequest)
                    }
                }
            }

            override fun onActivityCreated(a: Activity, s: Bundle?) {}
            override fun onActivityResumed(a: Activity) {}
            override fun onActivityPaused(a: Activity) {}
            override fun onActivitySaveInstanceState(a: Activity, out: Bundle) {}
            override fun onActivityDestroyed(a: Activity) {}
        })

        // Iniciar Schedulers (Tareas periódicas)
        LogScheduler(this).schedulePeriodicUpload()

        // Sincronización inicial de Remote Config
        val initialSyncRequest = OneTimeWorkRequest.Builder(InitialSyncWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(initialSyncRequest)

        // Sincronización inicial de logs (por si quedaron pendientes de la sesión anterior)
        val logUploadRequest = OneTimeWorkRequest.Builder(LogUploadWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(logUploadRequest)
    }
}