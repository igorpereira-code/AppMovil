package ucb.edu.bo.events.data.mapper

import ucb.edu.bo.events.data.entity.EventEntity
import ucb.edu.bo.events.domain.model.EventModel

fun EventModel.toEntity() = EventEntity(type = type, timestamp = timestamp)
fun EventEntity.toModel() = EventModel(id, type, timestamp)