package ucb.edu.bo.events.data.service

import ucb.edu.bo.events.data.dao.EventDao
import ucb.edu.bo.events.data.datasource.EventLocalDataSource
import ucb.edu.bo.events.data.entity.EventEntity

class EventDbService(private val eventDao: EventDao) : EventLocalDataSource {
    override suspend fun save(entity: EventEntity) = eventDao.insert(entity)
    override suspend fun list(): List<EventEntity> = eventDao.getList()
    override suspend fun clear() = eventDao.deleteAll()
}