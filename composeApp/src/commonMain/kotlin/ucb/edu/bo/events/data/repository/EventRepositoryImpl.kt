package ucb.edu.bo.events.data.repository

import ucb.edu.bo.events.data.datasource.EventLocalDataSource
import ucb.edu.bo.events.data.mapper.toEntity
import ucb.edu.bo.events.data.mapper.toModel
import ucb.edu.bo.events.domain.model.EventModel
import ucb.edu.bo.events.domain.repository.EventRepository

class EventRepositoryImpl(private val localDataSource: EventLocalDataSource) : EventRepository {
    override suspend fun save(model: EventModel) = localDataSource.save(model.toEntity())
    override suspend fun getList(): List<EventModel> = localDataSource.list().map { it.toModel() }
    override suspend fun clear() = localDataSource.clear()
}