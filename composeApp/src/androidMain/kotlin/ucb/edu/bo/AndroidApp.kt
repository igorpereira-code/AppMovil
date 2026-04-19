package ucb.edu.bo

import android.app.Application
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ucb.edu.bo.di.getModules
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

        // Iniciar WorkManager periódico
        LogScheduler(this).schedulePeriodicUpload()


        val oneTimeRequest = OneTimeWorkRequest.Builder(LogUploadWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(oneTimeRequest)
    }
}