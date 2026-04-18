package ucb.edu.bo.dollar.presentation.state

import ucb.edu.bo.dollar.domain.model.DollarModel

data class DollarUIState(
    val list: List<DollarModel> = emptyList(),
    val isLoading: Boolean = true
)