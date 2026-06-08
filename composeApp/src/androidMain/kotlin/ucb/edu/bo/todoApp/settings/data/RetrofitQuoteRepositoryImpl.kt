package ucb.edu.bo.todoApp.settings.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ucb.edu.bo.todoApp.settings.domain.repository.QuoteRepository

// 1. El DTO para Gson
data class QuoteDto(val es: String, val en: String)

// 2. La Interfaz de Retrofit
interface QuoteApi {
    @GET("mjlozada2003/06cabe78e08ad2ebeda44e6560e94ed3/raw/8b0e0ab970c0ebbefd4329f7029604b641afc885/quotes.json")
    suspend fun getQuotes(): List<QuoteDto>
}

// 3. La Implementación Real
class RetrofitQuoteRepositoryImpl : QuoteRepository {

    // Construimos el cliente Retrofit clásico
    private val api = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuoteApi::class.java)

    override suspend fun getRandomMotivationalQuote(languageCode: String): String {
        return try {
            val quotesList = api.getQuotes()
            val randomQuote = quotesList.random()

            // Elegimos el idioma dinámicamente
            if (languageCode == "es") {
                randomQuote.es
            } else {
                randomQuote.en
            }
        } catch (e: Exception) {
            // Textos de fallback en ambos idiomas por si no hay internet
            if (languageCode == "es") {
                "Sigue adelante, enfócate en tu siguiente tarea."
            } else {
                "Keep going, focus on your next task."
            }
        }
    }
}