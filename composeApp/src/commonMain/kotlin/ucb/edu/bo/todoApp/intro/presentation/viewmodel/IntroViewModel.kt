package ucb.edu.bo.todoApp.intro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ucb.edu.bo.todoApp.intro.presentation.state.IntroState

class IntroViewModel : ViewModel() {

    private val _state = MutableStateFlow(IntroState())
    val state: StateFlow<IntroState> = _state.asStateFlow()

    fun nextPage() {
        val current = _state.value.currentPage
        if (current < _state.value.pages.size - 1) {
            _state.value = _state.value.copy(currentPage = current + 1)
        }
    }

    fun skipToLast() {
        _state.value = _state.value.copy(
            currentPage = _state.value.pages.size - 1
        )
    }
}