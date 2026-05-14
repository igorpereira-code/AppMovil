package ucb.edu.bo.app.home.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.add_image
import appmovil.composeapp.generated.resources.sort_image
import appmovil.composeapp.generated.resources.user
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.app.home.presentation.composable.*
import ucb.edu.bo.app.home.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Index", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(Res.drawable.sort_image),
                            contentDescription = "Sort",
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Image(
                            painter = painterResource(Res.drawable.user),
                            contentDescription = "Profile",
                            modifier = Modifier.size(42.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = { HomeBottomNavigation() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddTaskClicked() },
                containerColor = Color(0xFF8875FF),
                shape = CircleShape,
                modifier = Modifier.size(64.dp).offset(y = 32.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_image),
                    contentDescription = "Add",
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = Color.Black
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (uiState.tasks.isEmpty()) {
                EmptyHomeContent()
            } else {
                TaskList(uiState.tasks)
            }

            if (uiState.showAddTaskSheet) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onDismissAddTask() },
                    sheetState = sheetState,
                    containerColor = Color(0xFF363636),
                    dragHandle = null
                ) {
                    AddTaskSheetContent(
                        title = uiState.newTaskTitle,
                        description = uiState.newTaskDescription,
                        onTitleChange = viewModel::onTaskTitleChanged,
                        onDescriptionChange = viewModel::onTaskDescriptionChanged,
                        onTimerClick = viewModel::onTimerClicked,
                        onPriorityClick = viewModel::onPriorityClicked,
                        onSave = viewModel::onSaveTask
                    )
                }
            }

            if (uiState.showDatePicker) {
                TaskDatePicker(
                    onDateSelected = viewModel::onDateSelected,
                    onDismiss = viewModel::onDismissDatePicker
                )
            }

            if (uiState.showTimePicker) {
                TaskTimePicker(
                    onTimeSelected = viewModel::onTimeSelected,
                    onDismiss = viewModel::onDismissTimePicker
                )
            }

            if (uiState.showPriorityPicker) {
                TaskPriorityPicker(
                    selectedPriority = uiState.selectedPriority,
                    onPrioritySelected = viewModel::onPrioritySelected,
                    onSave = viewModel::onSavePriority,
                    onDismiss = viewModel::onDismissPriorityPicker
                )
            }
        }
    }
}
