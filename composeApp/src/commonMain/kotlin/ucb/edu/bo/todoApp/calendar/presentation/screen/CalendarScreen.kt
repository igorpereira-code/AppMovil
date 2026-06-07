package ucb.edu.bo.todoApp.calendar.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.Screen
import ucb.edu.bo.todoApp.calendar.presentation.composable.CalendarCarousel
import ucb.edu.bo.todoApp.task.presentation.composable.*
import ucb.edu.bo.todoApp.calendar.presentation.viewmodel.CalendarViewModel
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel
import ucb.edu.bo.todoApp.task.presentation.composable.DatePickerModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = koinViewModel(),
    taskViewModel: TaskViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val taskState by taskViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(taskState.tasks) {
        viewModel.loadTasksForSelectedDate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top Bar ──────────────────────────────────────────────────────────────
            Text(
                text = stringResource(Res.string.calendar_title),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // ── Carrusel de Fechas Dinámico ─────────────────────────────────────────
            CalendarCarousel(
                monthYearText = state.monthYearText,
                weekDays = state.currentWeekDays,
                selectedDate = state.selectedDate,
                onDateSelected = { viewModel.onDateSelected(it) },
                onPreviousWeek = { viewModel.onPreviousWeek() },
                onNextWeek = { viewModel.onNextWeek() },
                onMonthClick = { viewModel.showDatePicker() }
            )

            // ── Filtros (Today / Completed) ──────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xFF272727), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Button(
                    onClick = { viewModel.onTabSelected("Today") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.selectedTab == "Today") MaterialTheme.colorScheme.primary else Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(stringResource(Res.string.calendar_tab_today), color = Color.White, fontSize = 16.sp)
                }

                Button(
                    onClick = { viewModel.onTabSelected("Completed") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.selectedTab == "Completed") MaterialTheme.colorScheme.primary else Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(stringResource(Res.string.calendar_filter_completed), color = Color.White, fontSize = 16.sp)
                }
            }

            // ── Lista de Tareas Filtradas ────────────────────────────────────────────
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (state.filteredTasks.isEmpty()) {
                EmptyTasksContent(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp), // Espacio para el BottomBar
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.filteredTasks, key = { it.id }) { task ->
                        val timeString = viewModel.formatTaskTimeText(task.date, task.time)

                        // CORREGIDO: Buscamos la categoría real de la tarea para mostrar ícono y color
                        val category = taskState.categories.find { it.id == task.categoryId }

                        TaskItem(
                            title = task.title,
                            timeText = timeString,
                            priority = task.priority,
                            categoryName = category?.name,
                            categoryIcon = category?.iconResName,
                            categoryColor = category?.colorHex?.let { Color(it) } ?: MaterialTheme.colorScheme.primary,
                            isCompleted = task.isCompleted,
                            onToggle = { /* Lógica de tu ViewModel para completar tarea */ },
                            onClick = { /* Lógica para abrir la pantalla de detalles (Task Screen) */ }
                        )
                    }
                }
            }
        }

        // ── Bottom Nav Bar ───────────────────────────────────────────────────────
        BottomNavBar(
            currentRoute = "Calendario",
            onHomeClick = {
                navController.navigate(Screen.Task.route) {
                    popUpTo(Screen.Task.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onCalendarClick = { /* Ya estás aquí, no haces nada */ },
            onFocusClick = {
                navController.navigate(Screen.Focus.route) {
                    popUpTo(Screen.Task.route)
                    launchSingleTop = true
                }
            },
            onProfileClick = {
                // CORREGIDO: Navega a Profile
                navController.navigate(Screen.Profile.route) {
                    popUpTo(Screen.Task.route)
                    launchSingleTop = true
                }
            },
            onAddClick = { taskViewModel.showAddTaskSheet() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        // ── EL MODAL DE FECHAS (FLOTANTE) ────────────────────────────────────────
        // Se coloca al final para que flote sobre todo lo demás
        if (state.isDatePickerVisible) {
            DatePickerModal(
                initialDate = state.selectedDate,
                onDateSelected = { newDate ->
                    viewModel.onDatePickedFromModal(newDate)
                },
                onDismiss = { viewModel.hideDatePicker() }
            )
        }
    }

    AddTaskModalsGlobal(taskViewModel = taskViewModel)
}