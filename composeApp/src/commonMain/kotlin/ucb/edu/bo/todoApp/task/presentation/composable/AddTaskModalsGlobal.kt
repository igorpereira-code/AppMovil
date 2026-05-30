package ucb.edu.bo.todoApp.task.presentation.composable

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskModalsGlobal(taskViewModel: TaskViewModel) {
    val taskState by taskViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ── Add Task Bottom Sheet ────────────────────────────────────────────────────
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
                onSend = { taskViewModel.saveTask() },
                onTimeClick = { taskViewModel.showDatePicker() },
                onTagClick = { /* Pendiente: Tarea de tu compañero */ },
                onPriorityClick = { taskViewModel.showPriorityPicker() }
            )
        }
    }

    // ── Modales Superpuestos ──────────────────────────────────────────────────────
    if (taskState.isTimePickerVisible) {
        TimePickerModal(
            initialTime = taskState.selectedTime,
            onTimeSelected = { taskViewModel.onTimeSelected(it) },
            onDismiss = { taskViewModel.hideTimePicker() }
        )
    }

    if (taskState.isPriorityPickerVisible) {
        PriorityPickerModal(
            currentPriority = taskState.selectedPriority,
            onPrioritySelected = { taskViewModel.onPrioritySelected(it) },
            onDismiss = { taskViewModel.hidePriorityPicker() }
        )
    }

    if (taskState.isDatePickerVisible) {
        DatePickerModal(
            initialDate = taskState.selectedDate,
            onDateSelected = { taskViewModel.onDateSelected(it) },
            onDismiss = { taskViewModel.hideDatePicker() }
        )
    }
}