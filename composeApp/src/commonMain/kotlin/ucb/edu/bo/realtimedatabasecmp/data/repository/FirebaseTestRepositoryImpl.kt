package ucb.edu.bo.realtimedatabasecmp.data.repository

import ucb.edu.bo.realtimedatabasecmp.data.datasource.FirebaseDataSource
import ucb.edu.bo.realtimedatabasecmp.domain.repository.FirebaseTestRepository

class FirebaseTestRepositoryImpl(
    private val dataSource: FirebaseDataSource
) : FirebaseTestRepository {
    override suspend fun saveTest(path: String, value: String): Result<Unit> {
        return try {
            dataSource.saveTestValue(path, value)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}