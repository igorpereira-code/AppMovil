package ucb.edu.bo

import android.app.Application
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ucb.edu.bo.di.getModules
import ucb.edu.bo.formulario.data.worker.FormularioAutoSaveWorker
import ucb.edu.bo.remoteconfig.data.worker.RemoteConfigSyncWorker
import ucb.edu.bo.workmanager.LogScheduler
import java.util.concurrent.TimeUnit

class AndroidApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApp)
            modules(getModules())
        }

        // WorkManager periódico existente
        LogScheduler(this).schedulePeriodicUpload()

        // Sincronización inicial de Remote Config
        val remoteConfigSync = OneTimeWorkRequest.Builder(
            RemoteConfigSyncWorker::class.java
        ).build()
        WorkManager.getInstance(this).enqueue(remoteConfigSync)

        // ✅ Worker autoguardado formulario cada 15 minutos
        val formularioAutoSave = PeriodicWorkRequest.Builder(
            FormularioAutoSaveWorker::class.java,
            15L,
            TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(this).enqueue(formularioAutoSave)
    }
}