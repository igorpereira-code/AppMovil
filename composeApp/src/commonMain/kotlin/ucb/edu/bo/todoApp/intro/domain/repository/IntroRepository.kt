package ucb.edu.bo.todoApp.intro.domain.repository

import ucb.edu.bo.todoApp.intro.domain.model.IntroPage

interface IntroRepository {
    suspend fun getIntroPages(lang: String): List<IntroPage>
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun markOnboardingCompleted()
}