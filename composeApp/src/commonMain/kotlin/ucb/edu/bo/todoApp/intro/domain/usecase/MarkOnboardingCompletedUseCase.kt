package ucb.edu.bo.todoApp.intro.domain.usecase

import ucb.edu.bo.todoApp.intro.domain.repository.IntroRepository

class MarkOnboardingCompletedUseCase(private val repository: IntroRepository) {
    suspend operator fun invoke() = repository.markOnboardingCompleted()
}