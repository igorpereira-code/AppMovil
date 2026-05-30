package ucb.edu.bo.todoApp.focus_mode.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.todoApp.focus_mode.presentation.state.FocusState
import ucb.edu.bo.todoApp.focus_mode.presentation.viewmodel.FocusViewModel
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.navigation.NavHostController
import ucb.edu.bo.Screen
import ucb.edu.bo.todoApp.task.presentation.composable.*
// NUEVO: Importamos el ViewModel de tareas
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class) // NUEVO: Necesario para el ModalBottomSheet
@Composable
fun FocusScreen(
    onLogout: () -> Unit,
    viewModel: FocusViewModel = koinViewModel(),
    taskViewModel: TaskViewModel = koinViewModel(), // NUEVO: Inyectamos el TaskViewModel
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    val taskState by taskViewModel.state.collectAsState() // NUEVO: Escuchamos el estado de las tareas
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) // NUEVO

    // NUEVO: Envolvemos todo en un Box para anclar la barra de navegación abajo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(bottom = 72.dp), // Espacio para que la barra no tape las estadísticas
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header con título y botón cerrar sesión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Modo Enfoque",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onLogout) {
                    Text(
                        text = "Cerrar sesión",
                        color = Color(0xFFE74C3C),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Selector de minutos
            if (!state.isRunning && state.elapsedSeconds == 0) {
                TimeSelectorSection(
                    selectedMinutes = state.selectedMinutes,
                    onSelectMinutes = { viewModel.setSelectedMinutes(it) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Temporizador circular
            TimerSection(state = state)

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de control
            ControlButtons(
                state = state,
                onStart = { viewModel.startFocus() },
                onStop = { viewModel.stopFocus() },
                onReset = { viewModel.resetFocus() }
            )

            if (state.isSaving) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Guardando sesión...",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            state.saveError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(color = Color(0xFF2C2C2C))

            Spacer(modifier = Modifier.height(24.dp))

            // Estadísticas semanales
            StatsSection(state = state)
        }

        // ── Bottom Nav Bar ───────────────────────────────────────────────────────
        BottomNavBar(
            currentRoute = Screen.Focus.route,
            onHomeClick = {
                navController.navigate(Screen.Task.route) {
                    popUpTo(Screen.Task.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onCalendarClick = {
                navController.navigate(Screen.Calendar.route) {
                    popUpTo(Screen.Task.route) // Volvemos al root antes de abrir calendario
                    launchSingleTop = true
                }
            },
            onFocusClick = {},
            onProfileClick = {
                navController.navigate(Screen.Settings.route) {//Cambiar a perfil cuando haya
                    popUpTo(Screen.Task.route)
                    launchSingleTop = true
                }
            },
            onAddClick = { taskViewModel.showAddTaskSheet() }, // NUEVO: Llama al modal
            modifier = Modifier.align(Alignment.BottomCenter) // Ahora sí funciona porque está en un Box
        )
    }

    AddTaskModalsGlobal(taskViewModel = taskViewModel)
}

@Composable
fun TimeSelectorSection(
    selectedMinutes: Int,
    onSelectMinutes: (Int) -> Unit
) {
    val options = listOf(5, 10, 15, 25, 30, 45, 60)
    var customInput by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf("") }

    Text(
        text = "Selecciona el tiempo",
        color = Color.Gray,
        fontSize = 14.sp
    )
    Spacer(modifier = Modifier.height(12.dp))

    // Chips de opciones rápidas
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { minutes ->
            FilterChip(
                selected = selectedMinutes == minutes,
                onClick = {
                    onSelectMinutes(minutes)
                    customInput = ""
                    inputError = ""
                },
                label = {
                    Text(
                        text = "${minutes}m",
                        fontSize = 12.sp,
                        color = if (selectedMinutes == minutes) Color.White else Color.Gray
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color(0xFF1D1D1D)
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Input personalizado
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = customInput,
            onValueChange = {
                customInput = it.filter { c -> c.isDigit() }.take(3)
                inputError = ""
            },
            label = { Text("Minutos personalizados", color = Color.Gray, fontSize = 12.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFF444444),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp)
        )
        Button(
            onClick = {
                val mins = customInput.toIntOrNull()
                when {
                    mins == null || mins <= 0 -> inputError = "Ingresa un valor válido"
                    mins > 180 -> inputError = "Máximo 180 minutos"
                    else -> {
                        onSelectMinutes(mins)
                        inputError = ""
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "OK", color = Color.White)
        }
    }

    if (inputError.isNotEmpty()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = inputError,
            color = Color.Red,
            fontSize = 12.sp
        )
    }
}

@Composable
fun TimerSection(state: FocusState) {
    val targetSeconds = state.selectedMinutes * 60
    val remaining = targetSeconds - state.elapsedSeconds
    val minutes = remaining / 60
    val seconds = remaining % 60
    val progress = if (targetSeconds > 0) {
        state.elapsedSeconds.toFloat() / targetSeconds.toFloat()
    } else 0f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(220.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 8.dp,
            trackColor = Color(0xFF2C2C2C)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "%02d:%02d".format(minutes, seconds),
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (state.isRunning) "Enfocado" else "Listo",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ControlButtons(
    state: FocusState,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.isRunning) {
            Button(
                onClick = onStop,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(140.dp)
            ) {
                Text(text = "Detener", color = Color.White, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(140.dp)
            ) {
                Text(text = "Iniciar", color = Color.White, fontWeight = FontWeight.Bold)
            }
            if (state.elapsedSeconds > 0) {
                OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .width(120.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                ) {
                    Text(text = "Reiniciar", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun StatsSection(state: FocusState) {
    val days = listOf("LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM")

    Text(
        text = "Esta semana",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    if (state.isLoadingStats) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    } else {
        val maxMinutes = state.weekSessions
            .groupBy { it.dayOfWeek }
            .mapValues { entry -> entry.value.sumOf { it.durationMinutes } }
            .values.maxOrNull()?.takeIf { it > 0 } ?: 1

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            days.forEach { day ->
                val dayMinutes = state.weekSessions
                    .filter { it.dayOfWeek == day }
                    .sumOf { it.durationMinutes }
                val barHeight = ((dayMinutes.toFloat() / maxMinutes.toFloat()) * 100).dp

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f)
                ) {
                    if (dayMinutes > 0) {
                        Text(
                            text = "${dayMinutes}m",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 9.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(if (dayMinutes > 0) barHeight.coerceAtLeast(8.dp) else 8.dp)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(
                                if (dayMinutes > 0) MaterialTheme.colorScheme.primary else Color(0xFF2C2C2C)
                            )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = day,
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1D1D1D)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Promedio diario",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Esta semana",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
                Text(
                    text = "%.0f min".format(state.weeklyAverageMinutes),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
