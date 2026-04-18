package ucb.edu.bo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import ucb.edu.bo.di.getModules
import ucb.edu.bo.dollar.presentation.screen.DollarScreen

@Composable
@Preview
fun App(){
        MaterialTheme {
            DollarScreen()
        }

}