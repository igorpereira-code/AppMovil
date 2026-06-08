package ucb.edu.bo.workmanager

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import ucb.edu.bo.events.domain.usecase.BackgroundEventTrigger

class AndroidBackgroundEventTrigger(private val context: Context) : BackgroundEventTrigger {
    override fun triggerEventWorker(eventType: String) {
        // Le pasamos el texto al Worker
        val data = Data.Builder().putString("EVENT_TYPE", eventType).build()
        val request = OneTimeWorkRequest.Builder(LogUploadWorker::class.java)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}