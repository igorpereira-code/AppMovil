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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import ucb.edu.bo.todoApp.calendar.presentation.screen.CalendarScreen
import ucb.edu.bo.todoApp.focus_mode.presentation.screen.FocusScreen
import ucb.edu.bo.todoApp.intro.presentation.screen.IntroScreen
import ucb.edu.bo.todoApp.intro.presentation.screen.WelcomeScreen
import ucb.edu.bo.todoApp.login.presentation.screen.LoginScreen
import ucb.edu.bo.todoApp.login.presentation.screen.RegisterScreen
import ucb.edu.bo.todoApp.settings.presentation.screen.SettingsScreen
import ucb.edu.bo.todoApp.task.presentation.screen.TaskScreen
import ucb.edu.bo.todoApp.profile.presentation.screen.ProfileScreen // Tu pantalla de perfil
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import ucb.edu.bo.todoApp.task.presentation.screen.EditTaskScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Focus : Screen("focus")
    object Home : Screen("home")
    object Task : Screen("task")
    object Calendar : Screen("calendar")

    object Settings : Screen("settings")

    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int) = "edit_task/$taskId"
    }
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation(startDestination: String, navController: NavHostController) {
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
                    // Usamos SessionManager en lugar de Firebase
                    ucb.edu.bo.SessionManager.logout()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                // NUEVO: Agregamos el navController para que FocusScreen pueda navegar
                navController = navController
            )
        }

        composable(Screen.Task.route) {
            TaskScreen(navController = navController)
        }

        composable(Screen.Calendar.route) {
            CalendarScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF121212)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.home_welcome_message),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        composable(
            route = Screen.EditTask.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable
            EditTaskScreen(
                taskId = taskId,
                navController = navController
            )
        }

        // UN SOLO BLOQUE DE PROFILE SCREEN
        composable(Screen.Profile.route) {
            // Instanciamos el TaskViewModel aquí
            val taskViewModel: ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel = org.koin.compose.viewmodel.koinViewModel()

            ProfileScreen(
                navController = navController,
                taskViewModel = taskViewModel, // PASAMOS EL VIEWMODEL
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogoutSuccess = {
                    ucb.edu.bo.SessionManager.logout()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}
