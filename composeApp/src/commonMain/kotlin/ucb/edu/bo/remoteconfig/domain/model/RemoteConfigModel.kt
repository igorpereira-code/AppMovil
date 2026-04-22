package ucb.edu.bo.remoteconfig.domain.model

data class RemoteConfigModel(
    val key: String,
    val value: String,
    val updatedAt: Long
)