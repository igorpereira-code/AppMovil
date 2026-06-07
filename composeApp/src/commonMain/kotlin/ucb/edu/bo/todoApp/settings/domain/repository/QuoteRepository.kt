package ucb.edu.bo.todoApp.settings.domain.repository

interface QuoteRepository {
    suspend fun getRandomMotivationalQuote(): String
}