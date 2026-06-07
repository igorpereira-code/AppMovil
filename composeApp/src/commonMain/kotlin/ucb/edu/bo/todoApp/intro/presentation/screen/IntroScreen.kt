package ucb.edu.bo.todoApp.intro.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.todoApp.intro.presentation.viewmodel.IntroViewModel

@Composable
fun IntroScreen(
    onFinish: () -> Unit,
    viewModel: IntroViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentPage = state.pages[state.currentPage]
    val isLastPage = state.currentPage == state.pages.size - 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Botón Skip arriba a la derecha
        if (!isLastPage) {
            TextButton(
                onClick = { viewModel.skipToLast() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.intro_button_skip),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Imagen placeholder
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.large),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentPage.imageRes,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Título
            Text(
                text = stringResource(currentPage.title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = stringResource(currentPage.description),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Indicadores de página
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.pages.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(if (index == state.currentPage) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == state.currentPage) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.currentPage > 0) {
                    TextButton(onClick = {
                        // No hay función back en el viewmodel, podemos ignorar o agregar
                    }) {
                        Text(
                            text = stringResource(Res.string.intro_button_back),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                Button(
                    onClick = {
                        if (isLastPage) onFinish()
                        else viewModel.nextPage()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = if (isLastPage) stringResource(Res.string.intro_button_start) else stringResource(Res.string.intro_button_next),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
