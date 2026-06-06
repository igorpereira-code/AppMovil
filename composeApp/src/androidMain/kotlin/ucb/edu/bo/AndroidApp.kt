package ucb.edu.bo

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
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
import ucb.edu.bo.todoApp.focus_mode.notification.FocusNotificationHelper
import ucb.edu.bo.todoApp.task.data.service.SyncNotificationHelper
// NUEVO: Importamos el worker de sincronización de tareas
import ucb.edu.bo.todoApp.task.data.service.TaskSyncWorker
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

        // Canal de notificaciones Focus Mode
        FocusNotificationHelper.createNotificationChannel(this)

        // WorkManager periódico existente
        LogScheduler(this).schedulePeriodicUpload()

        // Sincronización inicial de Remote Config
        val remoteConfigSync = OneTimeWorkRequest.Builder(
            RemoteConfigSyncWorker::class.java
        ).build()
        WorkManager.getInstance(this).enqueue(remoteConfigSync)

        // Worker autoguardado formulario cada 15 minutos
        val formularioAutoSave = PeriodicWorkRequest.Builder(
            FormularioAutoSaveWorker::class.java,
            15L,
            TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(this).enqueue(formularioAutoSave)

        // ── NUEVO: WORKER DE SINCRONIZACIÓN DE TAREAS ────────────────────────
        SyncNotificationHelper.createChannel(this)

        // 1. Restricción: Solo sincronizar tareas a Firebase si hay internet
        val taskSyncConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // 2. Configurar periodicidad (Ej: cada 15 minutos)
        val taskSyncWork = PeriodicWorkRequest.Builder(
            TaskSyncWorker::class.java,
            15L,
            TimeUnit.MINUTES
        )
            .setConstraints(taskSyncConstraints)
            .build()

        // 3. Encolar de forma única para evitar duplicación de hilos en Android
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "TodoTaskSyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            taskSyncWork
        )

        // Temporalmente en AndroidApp.kt para dispararlo de inmediato
        val testSync = OneTimeWorkRequest.Builder(TaskSyncWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(testSync)
    }
}