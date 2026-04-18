package ucb.edu.bo.dollar.data.repository

import ucb.edu.bo.dollar.data.datasource.DollarLocalDataSource
import ucb.edu.bo.dollar.data.mapper.toEntity
import ucb.edu.bo.dollar.data.mapper.toModel
import ucb.edu.bo.dollar.domain.model.DollarModel
import ucb.edu.bo.dollar.domain.repository.DollarRepository

class DollarRepositoryImpl(
    val localDataSource: DollarLocalDataSource
): DollarRepository {
    override suspend fun save(model: DollarModel) {
        localDataSource.save(model.toEntity())
    }

    override suspend fun getList(): List<DollarModel> {
        return localDataSource.list().map {
            it.toModel()
        }
    }
}