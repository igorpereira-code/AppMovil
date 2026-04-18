package ucb.edu.bo.dollar.data.datasource

import ucb.edu.bo.dollar.data.entity.DollarEntity


interface DollarLocalDataSource {
    suspend fun save(entity: DollarEntity)
    suspend fun list() : List<DollarEntity>
}