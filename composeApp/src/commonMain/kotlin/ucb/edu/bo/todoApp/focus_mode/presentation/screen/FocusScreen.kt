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
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    onLogout: () -> Unit,
    viewModel: FocusViewModel = koinViewModel(),
    taskViewModel: TaskViewModel = koinViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.focus_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!state.isRunning && state.elapsedSeconds == 0) {
                TimeSelectorSection(
                    selectedMinutes = state.selectedMinutes,
                    onSelectMinutes = { viewModel.setSelectedMinutes(it) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            TimerSection(state = state)

            Spacer(modifier = Modifier.height(32.dp))

            ControlButtons(
                state = state,
                onStart = { viewModel.startFocus() },
                onStop = { viewModel.stopFocus() },
                onReset = { viewModel.resetFocus() }
            )

            if (state.isSaving) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(Res.string.focus_status_saving),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }

            state.saveError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(modifier = Modifier.height(24.dp))

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
                    popUpTo(Screen.Task.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onFocusClick = {},
            onProfileClick = {
                navController.navigate(Screen.Profile.route) {
                    popUpTo(Screen.Task.route)
                    launchSingleTop = true
                }
            },
            onAddClick = { taskViewModel.showAddTaskSheet() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    AddTaskModalsGlobal(taskViewModel = taskViewModel) // Corregido: Usar taskViewModel
}

@Composable
fun TimeSelectorSection(
    selectedMinutes: Int,
    onSelectMinutes: (Int) -> Unit
) {
    val options = listOf(5, 10, 15, 25, 30, 45, 60)
    var customInput by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf("") }
    
    val invalidValueMsg = stringResource(Res.string.error_invalid_value)
    val maxMinutesMsg = stringResource(Res.string.error_max_minutes)

    Text(
        text = stringResource(Res.string.focus_subtitle_select_time),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        fontSize = 14.sp
    )
    Spacer(modifier = Modifier.height(12.dp))

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
                        text = "${minutes}${stringResource(Res.string.focus_unit_min)}",
                        fontSize = 12.sp,
                        color = if (selectedMinutes == minutes) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

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
            label = { Text(stringResource(Res.string.focus_label_custom_minutes), fontSize = 12.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp)
        )
        Button(
            onClick = {
                val mins = customInput.toIntOrNull()
                when {
                    mins == null || mins <= 0 -> inputError = invalidValueMsg
                    mins > 180 -> inputError = maxMinutesMsg
                    else -> {
                        onSelectMinutes(mins)
                        inputError = ""
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = stringResource(Res.string.focus_button_ok))
        }
    }

    if (inputError.isNotEmpty()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = inputError,
            color = MaterialTheme.colorScheme.error,
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
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            @Suppress("DefaultLocale")
            Text(
                text = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (state.isRunning) stringResource(Res.string.focus_state_focused) else stringResource(Res.string.focus_state_ready),
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
                Text(text = stringResource(Res.string.focus_button_stop), color = Color.White, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(140.dp)
            ) {
                Text(text = stringResource(Res.string.focus_button_start), fontWeight = FontWeight.Bold)
            }
            if (state.elapsedSeconds > 0) {
                OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .width(120.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                ) {
                    Text(text = stringResource(Res.string.focus_button_restart), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun StatsSection(state: FocusState) {
    val days = listOf(
        stringResource(Res.string.focus_day_mon),
        stringResource(Res.string.focus_day_tue),
        stringResource(Res.string.focus_day_wed),
        stringResource(Res.string.focus_day_thu),
        stringResource(Res.string.focus_day_fri),
        stringResource(Res.string.focus_day_sat),
        stringResource(Res.string.focus_day_sun)
    )
    val dayLabels = listOf("LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM")

    Text(
        text = stringResource(Res.string.focus_stats_this_week),
        color = MaterialTheme.colorScheme.onBackground,
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
            dayLabels.forEachIndexed { index, day ->
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
                            text = "${dayMinutes}${stringResource(Res.string.focus_unit_min)}",
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
                                if (dayMinutes > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    @Suppress("DefaultLocale")
                    Text(
                        text = days[index],
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
                        text = stringResource(Res.string.focus_stats_daily_average),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                    Text(
                        text = stringResource(Res.string.focus_stats_this_week),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 11.sp
                    )
                }
                Text(
                    text = "${state.weeklyAverageMinutes.toInt()} ${stringResource(Res.string.focus_unit_min)}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
