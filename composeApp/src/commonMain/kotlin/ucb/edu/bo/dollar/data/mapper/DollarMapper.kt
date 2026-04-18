package ucb.edu.bo.dollar.data.mapper

import ucb.edu.bo.dollar.data.entity.DollarEntity
import ucb.edu.bo.dollar.domain.model.DollarModel

fun DollarModel.toEntity() = DollarEntity(
    dollarOfficial,
    dollarParallel
)

fun DollarEntity.toModel() =  DollarModel(
    id,
    dollarOfficial,
    dollarParallel
)