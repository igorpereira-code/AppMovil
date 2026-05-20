package ucb.edu.bo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun App() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) {
        Screen.Focus.route
    } else {
        Screen.Intro.route
    }

    MaterialTheme {
        AppNavigation(startDestination = startDestination)
    }
}