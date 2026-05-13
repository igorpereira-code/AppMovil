package ucb.edu.bo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ucb.edu.bo.intro.presentation.screen.IntroScreen
import ucb.edu.bo.intro.presentation.screen.WelcomeScreen
//import ucb.edu.bo.login.presentation.screen.LoginScreen
//import ucb.edu.bo.register.presentation.screen.RegisterScreen
//import ucb.edu.bo.focus.presentation.screen.FocusScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Focus : Screen("focus")
    object Home : Screen("home")
}

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Intro.route) {
            IntroScreen(
                onFinish = { navController.navigate(Screen.Welcome.route) }
            )
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLogin = { navController.navigate(Screen.Login.route) },
                onRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        /*composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                onGoToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screen.Home.route) },
                onGoToLogin = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Focus.route) {
            FocusScreen()
        }*/
        composable(Screen.Home.route) {
            // Aquí irá el home que hagan tus compañeros
        }
    }
}