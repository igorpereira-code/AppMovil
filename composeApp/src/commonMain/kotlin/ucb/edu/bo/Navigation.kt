package ucb.edu.bo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import ucb.edu.bo.todoApp.focus_mode.presentation.screen.FocusScreen
import ucb.edu.bo.todoApp.intro.presentation.screen.IntroScreen
import ucb.edu.bo.todoApp.intro.presentation.screen.WelcomeScreen
import ucb.edu.bo.todoApp.login.presentation.screen.LoginScreen
import ucb.edu.bo.todoApp.login.presentation.screen.RegisterScreen
import ucb.edu.bo.todoApp.task.presentation.screen.TaskScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Focus : Screen("focus")
    object Home : Screen("home")
    object Task : Screen("task")
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
                onFinish = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLogin = { navController.navigate(Screen.Login.route) },
                onRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Task.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Task.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Focus.route) {
            FocusScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Task.route) {
            TaskScreen()
        }

        composable(Screen.Home.route) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF121212)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡Bienvenido! 🎉",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
