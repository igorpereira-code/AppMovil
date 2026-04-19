package ucb.edu.bo.realtimedatabasecmp.domain.usecase

import ucb.edu.bo.realtimedatabasecmp.domain.repository.FirebaseTestRepository

class SaveTestDataUseCase(
    private val repository: FirebaseTestRepository
) {
    suspend operator fun invoke(path: String, value: String): Result<Unit> {
        return repository.saveTest(path, value)
    }
}