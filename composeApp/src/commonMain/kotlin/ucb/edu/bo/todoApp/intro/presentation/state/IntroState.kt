package ucb.edu.bo.todoApp.intro.presentation.state

import ucb.edu.bo.todoApp.intro.domain.model.IntroPage

data class IntroState(
    val pages: List<IntroPage> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val currentPage: IntroPage? get() = pages.getOrNull(currentIndex)
    val isFirstPage: Boolean get() = currentIndex == 0
    val isLastPage: Boolean get() = pages.isNotEmpty() && currentIndex == pages.lastIndex
}