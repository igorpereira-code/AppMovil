package ucb.edu.bo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ucb.edu.bo.app.home.presentation.screen.HomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.Home) {
        composable<NavRoute.Home> {
            HomeScreen()
        }
        composable<NavRoute.Profile> {
            // ProfileScreen()
        }
    }
}
