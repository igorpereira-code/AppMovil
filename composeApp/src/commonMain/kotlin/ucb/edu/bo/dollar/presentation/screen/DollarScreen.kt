package ucb.edu.bo.dollar.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.dollar.presentation.viewmodel.DollarViewModel

@Composable
fun DollarScreen(
    viewModel: DollarViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    if(state.value.isLoading) {
        CircularProgressIndicator()
    } else {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(stringResource(Res.string.dollar_records_count, state.value.list.size))
        }
    }

}