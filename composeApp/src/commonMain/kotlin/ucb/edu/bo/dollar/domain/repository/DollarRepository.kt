package ucb.edu.bo.dollar.domain.repository

import ucb.edu.bo.dollar.domain.model.DollarModel

interface DollarRepository {
    suspend fun save(model: DollarModel)
    suspend fun getList(): List<DollarModel>
}