package ucb.edu.bo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ucb.edu.bo.todoApp.calendar.presentation.screen.CalendarScreen
import ucb.edu.bo.todoApp.focus_mode.presentation.screen.FocusScreen
import ucb.edu.bo.todoApp.intro.presentation.screen.IntroScreen
import ucb.edu.bo.todoApp.intro.presentation.screen.WelcomeScreen
import ucb.edu.bo.todoApp.login.presentation.screen.LoginScreen
import ucb.edu.bo.todoApp.login.presentation.screen.RegisterScreen
import ucb.edu.bo.todoApp.settings.presentation.screen.SettingsScreen
import ucb.edu.bo.todoApp.task.presentation.screen.TaskScreen
import ucb.edu.bo.todoApp.profile.presentation.screen.ProfileScreen
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.todoApp.focus_mode.presentation.viewmodel.FocusViewModel
import ucb.edu.bo.todoApp.maintenance.presentation.screen.MaintenanceScreen
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
fun AppNavigation(
    startDestination: String,
    navController: NavHostController,
    remoteConfigViewModel: ucb.edu.bo.remoteconfig.presentation.viewmodel.RemoteConfigViewModel= koinViewModel()
) {
    // Escuchamos la variable de Firebase en tiempo real
    val isMaintenance by remoteConfigViewModel.isMaintenanceMode.collectAsState()
    // 1. EL BLINDAJE: Instanciamos el cronómetro a nivel global de la app
    val globalFocusViewModel: FocusViewModel = koinInject()

    // LA MAGIA DEL BLOQUEO: Si es true, dibujamos la pantalla de mantenimiento
    if (isMaintenance) {
        MaintenanceScreen()
    } else {

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
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
                val profileViewModel: ucb.edu.bo.todoApp.profile.presentation.viewmodel.ProfileViewModel =
                    org.koin.compose.viewmodel.koinViewModel()
                FocusScreen(
                    onLogout = {
                        // Cierra sesión real a través de Firebase usando el ViewModel
                        profileViewModel.logout {
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    navController = navController,
                    viewModel = globalFocusViewModel
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
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.home_welcome_message),
                        color = MaterialTheme.colorScheme.onBackground,
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

            composable(Screen.Profile.route) {
                val taskViewModel: ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel =
                    org.koin.compose.viewmodel.koinViewModel()
                val profileViewModel: ucb.edu.bo.todoApp.profile.presentation.viewmodel.ProfileViewModel =
                    org.koin.compose.viewmodel.koinViewModel()

                ProfileScreen(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    viewModel = profileViewModel, // Pasamos el ProfileViewModel a la pantalla
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onLogoutSuccess = {
                        // Cierra sesión real a través de Firebase usando el ViewModel
                        profileViewModel.logout {
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }
        }
    }
}