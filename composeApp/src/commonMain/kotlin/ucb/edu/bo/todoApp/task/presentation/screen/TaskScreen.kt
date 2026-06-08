package ucb.edu.bo.todoApp.task.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.Screen
import ucb.edu.bo.todoApp.task.presentation.composable.*
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = koinViewModel(),
    navController: androidx.navigation.NavHostController,
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Top Bar ──────────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.sort_image),
                contentDescription = stringResource(Res.string.task_desc_menu),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(Res.string.task_title_index),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.user),
                    contentDescription = stringResource(Res.string.task_desc_profile),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ── Contenido principal ──────────────────────────────────────────────────
        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (state.tasks.isEmpty()) {
            EmptyTasksContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 72.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 72.dp, bottom = 80.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.tasks) { task ->
                    val timeString = viewModel.formatTaskTimeText(task.date, task.time)
                        ?: stringResource(Res.string.no_calendar_program)
                    val category = state.categories.find { it.id == task.categoryId }

                    TaskItem(
                        title = task.title,
                        timeText = timeString,
                        priority = task.priority,
                        categoryName = category?.name,
                        categoryIcon = category?.iconResName,
                        categoryColor = category?.colorHex?.let { Color(it) } ?: MaterialTheme.colorScheme.primary,
                        isCompleted = task.isCompleted,
                        onToggle = { viewModel.toggleTask(task.id, !task.isCompleted) },
                        onClick = { navController.navigate(Screen.EditTask.createRoute(task.id)) }
                    )
                }
            }
        }

        // ── Bottom Nav Bar ───────────────────────────────────────────────────────
        BottomNavBar(
            currentRoute = "Index",
            onHomeClick = { /* Ya estás aquí */ },
            onCalendarClick = {
                navController.navigate(Screen.Calendar.route) {
                    popUpTo(Screen.Task.route)
                    launchSingleTop = true
                }
            },
            onFocusClick = {
                navController.navigate(Screen.Focus.route) {
                    popUpTo(Screen.Task.route)
                    launchSingleTop = true
                }
            },
            onProfileClick = {
                navController.navigate(Screen.Profile.route) {
                    popUpTo(Screen.Task.route)
                    launchSingleTop = true
                }
            },
            onAddClick = { viewModel.showAddTaskSheet() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    AddTaskModalsGlobal(taskViewModel = viewModel)
}
