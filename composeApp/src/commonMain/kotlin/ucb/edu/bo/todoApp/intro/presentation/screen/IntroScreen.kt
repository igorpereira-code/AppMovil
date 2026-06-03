package ucb.edu.bo.todoApp.intro.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.intro_img1
import appmovil.composeapp.generated.resources.intro_img2
import appmovil.composeapp.generated.resources.intro_img3
import org.jetbrains.compose.resources.painterResource
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

    val imageRes = when (state.currentPage) {
        0 -> Res.drawable.intro_img1
        1 -> Res.drawable.intro_img2
        else -> Res.drawable.intro_img3
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // Botón Omitir arriba a la derecha
        if (!isLastPage) {
            TextButton(
                onClick = { viewModel.skipToLast() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Omitir",
                    color = Color(0xFF8687E7),
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
            // Imagen
            Image(
                painter = painterResource(imageRes),
                contentDescription = currentPage.title,
                modifier = Modifier
                    .size(250.dp)
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Título
            Text(
                text = currentPage.title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = currentPage.description,
                color = Color.Gray,
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
                                if (index == state.currentPage) Color(0xFF8687E7)
                                else Color(0xFF444444)
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
                    TextButton(onClick = { viewModel.previousPage() }) {
                        Text(
                            text = "Atrás",
                            color = Color(0xFF8687E7),
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
                        containerColor = Color(0xFF8687E7)
                    )
                ) {
                    Text(
                        text = if (isLastPage) "Comenzar" else "Siguiente",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}