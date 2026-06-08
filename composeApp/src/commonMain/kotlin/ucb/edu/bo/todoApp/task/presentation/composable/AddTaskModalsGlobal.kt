package ucb.edu.bo.todoApp.task.presentation.composable

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ucb.edu.bo.todoApp.category.presentation.screen.CategoryScreen
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskModalsGlobal(taskViewModel: TaskViewModel) {
    val taskState by taskViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (taskState.isAddTaskSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { taskViewModel.hideAddTaskSheet() },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
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
                onTagClick = { taskViewModel.showCategoryPicker() },
                onPriorityClick = { taskViewModel.showPriorityPicker() }
            )
        }
    }

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

    if (taskState.isCategoryPickerVisible) {
        Dialog(
            onDismissRequest = { taskViewModel.hideCategoryPicker() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            CategoryScreen(
                onCategorySelected = { categoryId ->
                    taskViewModel.onCategorySelected(categoryId)
                },
                onClose = {
                    taskViewModel.hideCategoryPicker()
                }
            )
        }
    }
}
