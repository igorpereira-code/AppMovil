package ucb.edu.bo.todoApp.intro.domain.model

import org.jetbrains.compose.resources.StringResource

data class IntroPage(
    val title: StringResource,
    val description: StringResource,
    val imageRes: String
)