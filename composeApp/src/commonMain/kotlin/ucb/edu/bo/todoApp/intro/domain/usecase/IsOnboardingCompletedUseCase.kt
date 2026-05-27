package ucb.edu.bo.todoApp.intro.domain.usecase

import ucb.edu.bo.todoApp.intro.domain.repository.IntroRepository

class IsOnboardingCompletedUseCase(private val repository: IntroRepository) {
    suspend operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}