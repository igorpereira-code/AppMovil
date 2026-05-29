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
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.Screen
import ucb.edu.bo.todoApp.calendar.presentation.composable.CalendarCarousel
import ucb.edu.bo.todoApp.task.presentation.composable.*
import ucb.edu.bo.todoApp.calendar.presentation.viewmodel.CalendarViewModel
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top Bar ──────────────────────────────────────────────────────────────
            Text(
                text = "Calendar",
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
                onNextWeek = { viewModel.onNextWeek() }
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
                        containerColor = if (state.selectedTab == "Today") PrimaryPurple else Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Today", color = Color.White, fontSize = 16.sp)
                }

                Button(
                    onClick = { viewModel.onTabSelected("Completed") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.selectedTab == "Completed") PrimaryPurple else Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Completed", color = Color.White, fontSize = 16.sp)
                }
            }

            // ── Lista de Tareas Filtradas ────────────────────────────────────────────
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryPurple)
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
                    // Iteramos sobre todas las tareas filtradas
                    items(state.filteredTasks, key = { it.id }) { task ->

                        // 1. INTEGRACIÓN: Calculamos el texto de la hora para ESTA tarea en específico
                        val timeString = viewModel.formatTaskTimeText(task.date, task.time)

                        // 2. Dibujamos tu nuevo TaskItem pasándole ese texto
                        TaskItem(
                            title = task.title,
                            timeText = timeString,           // <-- Aquí le pasamos "Today At 16:45"
                            priority = task.priority,        // <-- Aquí le pasamos el número de la banderita
                            categoryName = null,             // <-- Lo dejamos en null hasta que tu compañero termine las categorías
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
                    popUpTo(Screen.Task.route){inclusive=true}
                    launchSingleTop = true
                }
                          },
            onCalendarClick = { /* Ya estás aquí, no haces nada */ },
            onFocusClick = {
                navController.navigate(Screen.Focus.route) {
                    popUpTo(Screen.Focus.route)
                    launchSingleTop = true
                    }
            },
            onAddClick = { taskViewModel.showAddTaskSheet()},
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
    if (taskState.isAddTaskSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { taskViewModel.hideAddTaskSheet() },
            sheetState = sheetState,
            containerColor = BottomSheetDark,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            AddTaskSheetContent(
                title = taskState.newTaskTitle,
                description = taskState.newTaskDescription,
                isSaving = taskState.isSaving,
                errorMessage = taskState.saveError,
                onTitleChange = { taskViewModel.onTitleChange(it) },
                onDescriptionChange = { taskViewModel.onDescriptionChange(it) },
                onSend = {
                    taskViewModel.saveTask()
                    viewModel.onDateSelected(state.selectedDate!!) // Opcional: Refresca el calendario al guardar
                },
                onTimeClick = { taskViewModel.showDatePicker() },
                onTagClick = { /* Pendiente */ },
                onPriorityClick = { taskViewModel.showPriorityPicker() }
            )
        }
    }
}