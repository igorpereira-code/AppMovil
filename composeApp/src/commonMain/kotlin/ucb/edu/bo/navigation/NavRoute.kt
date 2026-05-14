package ucb.edu.bo.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable
    object Home : NavRoute()

    @Serializable
    object Profile : NavRoute()
}
