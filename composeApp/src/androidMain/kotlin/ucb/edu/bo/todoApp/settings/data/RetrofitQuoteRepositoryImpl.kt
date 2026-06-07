package ucb.edu.bo.todoApp.settings.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ucb.edu.bo.todoApp.settings.domain.repository.QuoteRepository

// 1. El DTO para Gson
data class QuoteDto(val quote: String)

// 2. La Interfaz de Retrofit
interface QuoteApi {
    @GET("mjlozada2003/06cabe78e08ad2ebeda44e6560e94ed3/raw/087b72878169c643a1d12089800287b9c50b45c9/quotes.json")
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

    override suspend fun getRandomMotivationalQuote(): String {
        return try {
            val quotesList = api.getQuotes()
            quotesList.random().quote // ¡Toma una frase al azar de tu propio JSON!
        } catch (e: Exception) {
            "Sigue adelante, enfócate en tu siguiente tarea." // Fallback
        }
    }
}