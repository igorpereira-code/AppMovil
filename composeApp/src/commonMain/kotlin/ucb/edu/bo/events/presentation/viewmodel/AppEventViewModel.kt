package ucb.edu.bo.events.presentation.viewmodel

import androidx.lifecycle.ViewModel
import ucb.edu.bo.events.domain.usecase.BackgroundEventTrigger

class AppEventViewModel(
    private val backgroundEventTrigger: BackgroundEventTrigger
) : ViewModel() {
    fun onAppOpened() = backgroundEventTrigger.triggerEventWorker("Open")
    fun onAppClosed() = backgroundEventTrigger.triggerEventWorker("Close")

}