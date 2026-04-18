package ucb.edu.bo.dollar.domain.usecase

import ucb.edu.bo.dollar.domain.model.DollarModel
import ucb.edu.bo.dollar.domain.repository.DollarRepository

class CreateDollarUseCase(
    private val repository: DollarRepository
) {

    suspend fun invoke(model: DollarModel) {
        repository.save(model)
    }
}