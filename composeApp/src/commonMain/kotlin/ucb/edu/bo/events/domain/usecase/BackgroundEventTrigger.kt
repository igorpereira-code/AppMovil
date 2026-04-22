package ucb.edu.bo.events.domain.usecase

interface BackgroundEventTrigger {
    fun triggerEventWorker(eventType: String)
}