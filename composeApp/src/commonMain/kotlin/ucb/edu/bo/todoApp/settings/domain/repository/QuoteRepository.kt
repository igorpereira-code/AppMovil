package ucb.edu.bo.todoApp.settings.domain.repository

interface QuoteRepository {
    suspend fun getRandomMotivationalQuote(languageCode: String): String
}