package ucb.edu.bo.todoApp.focus_mode.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.focus_mode.domain.model.FocusSession
import ucb.edu.bo.todoApp.focus_mode.domain.usecase.GetWeekSessionsUseCase
import ucb.edu.bo.todoApp.focus_mode.domain.usecase.SaveFocusSessionUseCase
import ucb.edu.bo.todoApp.focus_mode.notification.FocusNotifier
import ucb.edu.bo.todoApp.focus_mode.presentation.state.FocusState

class FocusViewModel(
    private val saveSessionUseCase: SaveFocusSessionUseCase,
    private val getWeekSessionsUseCase: GetWeekSessionsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(FocusState())
    val state: StateFlow<FocusState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private val notifier = FocusNotifier()

    init {
        loadWeekStats()
    }

    fun startFocus() {
        if (_state.value.isRunning) return
        _state.value = _state.value.copy(isRunning = true)
        val targetSeconds = _state.value.selectedMinutes * 60
        val remainingInit = targetSeconds - _state.value.elapsedSeconds
        notifier.startTimerNotification(formatTime(remainingInit))

        timerJob = viewModelScope.launch {
            while (_state.value.isRunning) {
                delay(1000L)
                val newElapsed = _state.value.elapsedSeconds + 1
                _state.value = _state.value.copy(elapsedSeconds = newElapsed)
                val remaining = targetSeconds - newElapsed
                // Llamada al notificador
                notifier.updateTimerNotification(formatTime(remaining))

                if (newElapsed >= targetSeconds) {
                    stopAndSave()
                    break
                }
            }
        }
    }

    fun stopFocus() {
        if (!_state.value.isRunning) return
        stopAndSave()
    }

    fun resetFocus() {
        timerJob?.cancel()
        notifier.stopTimerNotification()
        _state.value = _state.value.copy(
            isRunning = false,
            elapsedSeconds = 0
        )
    }

    fun setSelectedMinutes(minutes: Int) {
        if (!_state.value.isRunning) {
            _state.value = _state.value.copy(selectedMinutes = minutes)
        }
    }

    private fun stopAndSave() {
        timerJob?.cancel()
        notifier.stopTimerNotification()
        val elapsed = _state.value.elapsedSeconds
        val selectedMinutes = _state.value.selectedMinutes
        _state.value = _state.value.copy(isRunning = false)

        val minutesElapsed = elapsed / 60
        if (minutesElapsed >= 1) {
            if (elapsed >= selectedMinutes * 60) {
                notifier.notify(selectedMinutes)
            }
            saveSession(minutesElapsed)
        } else {
            _state.value = _state.value.copy(elapsedSeconds = 0)
        }
    }

    private fun formatTime(totalSeconds: Int): String {
        val m = totalSeconds / 60
        val s = totalSeconds % 60
        return "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
    }

    private fun saveSession(minutes: Int) {
        _state.value = _state.value.copy(isSaving = true)
        viewModelScope.launch {
            val session = FocusSession(
                id = generateSessionId(),
                date = getCurrentDateString(),
                dayOfWeek = getDayOfWeekString(),
                durationMinutes = minutes
            )
            saveSessionUseCase(session)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        elapsedSeconds = 0,
                        saveError = null
                    )
                    loadWeekStats()
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        elapsedSeconds = 0,
                        saveError = it.message
                    )
                }
        }
    }

    fun loadWeekStats() {
        _state.value = _state.value.copy(isLoadingStats = true)
        viewModelScope.launch {
            getWeekSessionsUseCase()
                .onSuccess { sessions ->
                    val average = if (sessions.isNotEmpty()) {
                        sessions.sumOf { it.durationMinutes }.toDouble() / 7.0
                    } else 0.0
                    _state.value = _state.value.copy(
                        weekSessions = sessions,
                        weeklyAverageMinutes = average,
                        isLoadingStats = false
                    )
                }
                .onFailure {
                    _state.value = _state.value.copy(isLoadingStats = false)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}