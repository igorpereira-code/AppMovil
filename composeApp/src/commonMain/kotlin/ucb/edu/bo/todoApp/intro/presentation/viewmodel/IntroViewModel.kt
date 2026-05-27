package ucb.edu.bo.todoApp.intro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucb.edu.bo.todoApp.intro.domain.usecase.GetIntroPagesUseCase
import ucb.edu.bo.todoApp.intro.domain.usecase.MarkOnboardingCompletedUseCase
import ucb.edu.bo.todoApp.intro.presentation.state.IntroState

class IntroViewModel(
    private val getIntroPages: GetIntroPagesUseCase,
    private val markOnboardingCompleted: MarkOnboardingCompletedUseCase,
    private val lang: String          // inyectado desde Koin
) : ViewModel() {

    private val _state = MutableStateFlow(IntroState())
    val state: StateFlow<IntroState> = _state.asStateFlow()

    // Eventos de navegación hacia la pantalla
    sealed class Event {
        object NavigateToHome : Event()
    }
    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    init {
        loadPages()
    }

    private fun loadPages() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val pages = getIntroPages(lang)
                _state.update { it.copy(pages = pages, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onNext() {
        _state.update { s ->
            if (s.currentIndex < s.pages.lastIndex)
                s.copy(currentIndex = s.currentIndex + 1)
            else s
        }
    }

    fun onPrevious() {
        _state.update { s ->
            if (s.currentIndex > 0) s.copy(currentIndex = s.currentIndex - 1)
            else s
        }
    }

    /** Omitir: navega sin guardar nada */
    fun onSkip() {
        viewModelScope.launch {
            _events.emit(Event.NavigateToHome)
        }
    }

    /** Iniciar (última página): guarda y navega */
    fun onStart() {
        viewModelScope.launch {
            markOnboardingCompleted()
            _events.emit(Event.NavigateToHome)
        }
    }
}