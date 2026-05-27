package ucb.edu.bo.todoApp.intro.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
//import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.todoApp.intro.presentation.viewmodel.IntroViewModel

@Composable
fun IntroScreen(
    onNavigateToHome: () -> Unit,
    viewModel: IntroViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // Escuchar eventos de navegación
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is IntroViewModel.Event.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    val pagerState = rememberPagerState(pageCount = { state.pages.size })

    // Sincronizar pager con el índice del ViewModel
    LaunchedEffect(state.currentIndex) {
        if (pagerState.currentPage != state.currentIndex) {
            pagerState.animateScrollToPage(state.currentIndex)
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        // Swipe manual → actualizar ViewModel (opcional, si quieres swipe libre)
        // viewModel.setPage(pagerState.currentPage)
    }

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.error != null && state.pages.isEmpty() -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error al cargar. Verifica tu conexión.")
            }
        }
        else -> {
            Scaffold { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Botón Omitir arriba a la derecha
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        if (!state.isLastPage) {
                            TextButton(onClick = { viewModel.onSkip() }) {
                                Text("Omitir")
                            }
                        }
                    }

                    // Pager
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f),
                        userScrollEnabled = false // controlado por botones
                    ) { page ->
                        val introPage = state.pages.getOrNull(page)
                        if (introPage != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Imagen (placeholder si URL vacía)
                                Box(
                                    modifier = Modifier
                                        .size(220.dp)
                                        .padding(bottom = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (introPage.imageUrl.isNotBlank()) {
                                        // Si tienes Coil: AsyncImage(model = introPage.imageUrl, ...)
                                        Text("🖼", style = MaterialTheme.typography.displayLarge)
                                    } else {
                                        Text("🖼", style = MaterialTheme.typography.displayLarge)
                                    }
                                }

                                Text(
                                    text = introPage.title,
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = introPage.description,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Indicadores de página
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        state.pages.indices.forEach { i ->
                            val selected = i == state.currentIndex
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(if (selected) 10.dp else 8.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    shape = MaterialTheme.shapes.small,
                                    color = if (selected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                ) {}
                            }
                        }
                    }

                    // Botones de navegación
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botón Anterior (oculto en primera página)
                        if (!state.isFirstPage) {
                            OutlinedButton(onClick = {
                                scope.launch {
                                    viewModel.onPrevious()
                                }
                            }) {
                                Text("Anterior")
                            }
                        } else {
                            Spacer(Modifier.width(100.dp))
                        }

                        // Botón Siguiente o Iniciar
                        if (state.isLastPage) {
                            Button(onClick = { viewModel.onStart() }) {
                                Text("Iniciar")
                            }
                        } else {
                            Button(onClick = {
                                scope.launch { viewModel.onNext() }
                            }) {
                                Text("Siguiente")
                            }
                        }
                    }
                }
            }
        }
    }
}