package ucb.edu.bo.todoApp.task.presentation.screen

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.todoApp.task.presentation.composable.*
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // ── Top Bar ──────────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size), contentDescription = "Menú", tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Index", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.weight(1f))
            // Avatar Placeholder
            Surface(modifier = Modifier.size(36.dp), shape = androidx.compose.foundation.shape.CircleShape, color = Color(0xFF444444)) {}
        }

        // ── Contenido principal ──────────────────────────────────────────────────
        if (state.isLoading) {
            CircularProgressIndicator(color = PrimaryPurple, modifier = Modifier.align(Alignment.Center))
        } else if (state.tasks.isEmpty()) {
            EmptyTasksContent(modifier = Modifier.fillMaxSize().padding(top = 72.dp))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 72.dp, bottom = 80.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.tasks, key = { it.id }) { task ->
                    TaskItem(
                        title = task.title,
                        description = task.description,
                        isCompleted = task.isCompleted,
                        onToggle = { viewModel.toggleTask(task.id, !task.isCompleted) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }

        // ── Bottom Nav Bar ───────────────────────────────────────────────────────
        BottomNavBar(
            onAddClick = { viewModel.showAddTaskSheet() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // ── Add Task Bottom Sheet ────────────────────────────────────────────────────
    if (state.isAddTaskSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideAddTaskSheet() },
            sheetState = sheetState,
            containerColor = BottomSheetDark,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            AddTaskSheetContent(
                title = state.newTaskTitle,
                description = state.newTaskDescription,
                isSaving = state.isSaving,
                errorMessage = state.saveError,
                onTitleChange = { viewModel.onTitleChange(it) },
                onDescriptionChange = { viewModel.onDescriptionChange(it) },
                onSend = { viewModel.saveTask() }
            )
        }
    }
}