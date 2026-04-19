package ucb.edu.bo.realtimedatabasecmp.domain.repository

interface FirebaseTestRepository {
    suspend fun saveTest(path: String, value: String): Result<Unit>
}