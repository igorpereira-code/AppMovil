package ucb.edu.bo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import ucb.edu.bo.todoApp.focus_mode.presentation.screen.FocusScreen
import ucb.edu.bo.todoApp.intro.data.repository.dataStore
import ucb.edu.bo.todoApp.intro.presentation.screen.IntroScreen
import ucb.edu.bo.todoApp.intro.presentation.screen.WelcomeScreen
import ucb.edu.bo.todoApp.login.presentation.screen.LoginScreen
import ucb.edu.bo.todoApp.login.presentation.screen.RegisterScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Focus : Screen("focus")
    object Home : Screen("home")
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    var startDestination by remember { mutableStateOf<String?>(null) }

    // Determinar destino inicial
    LaunchedEffect(Unit) {
        val isOnboardingDone = context.dataStore.data.first()[
            booleanPreferencesKey("onboarding_completed")
        ] ?: false
        val currentUser = FirebaseAuth.getInstance().currentUser

        startDestination = when {
            isOnboardingDone && currentUser != null -> Screen.Focus.route
            isOnboardingDone -> Screen.Welcome.route
            else -> Screen.Intro.route
        }
    }

    if (startDestination == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {
        composable(Screen.Intro.route) {
            IntroScreen(
                onNavigateToHome = {
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
                    navController.navigate(Screen.Focus.route) {
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
                    navController.navigate(Screen.Focus.route) {
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