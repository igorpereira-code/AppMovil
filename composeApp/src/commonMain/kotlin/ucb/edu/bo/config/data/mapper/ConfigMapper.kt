package ucb.edu.bo.config.data.mapper

import ucb.edu.bo.config.data.entity.ConfigEntity
import ucb.edu.bo.config.domain.model.ConfigModel

fun ConfigEntity.toModel() = ConfigModel(
    key = key,
    value = value
)