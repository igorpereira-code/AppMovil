package ucb.edu.bo.todoApp.intro.domain.usecase

import ucb.edu.bo.todoApp.intro.domain.model.IntroPage
import ucb.edu.bo.todoApp.intro.domain.repository.IntroRepository

class GetIntroPagesUseCase(private val repository: IntroRepository) {
    suspend operator fun invoke(lang: String): List<IntroPage> =
        repository.getIntroPages(lang)
}