package ucb.edu.bo.todoApp.task.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import appmovil.composeapp.generated.resources.*
import ucb.edu.bo.todoApp.task.presentation.composable.DatePickerModal
import ucb.edu.bo.todoApp.task.presentation.composable.PriorityPickerModal
import ucb.edu.bo.todoApp.task.presentation.composable.TimePickerModal
import ucb.edu.bo.todoApp.task.presentation.viewmodel.EditTaskViewModel
import ucb.edu.bo.todoApp.task.presentation.composable.EditTaskCategoryPickerModal
import ucb.edu.bo.todoApp.task.presentation.composable.EditTitleDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Int,
    navController: NavHostController,
    viewModel: EditTaskViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Cargar la tarea al entrar a la pantalla
    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    // Navegar de regreso cuando se guarda o elimina
    LaunchedEffect(state.isSavedSuccessfully) {
        if (state.isSavedSuccessfully) {
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // ── Top Bar ──────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(Res.drawable.cancel),
                            contentDescription = stringResource(Res.string.edit_task_cd_close),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(Res.drawable.repeat),
                        contentDescription = stringResource(Res.string.edit_task_cd_repeat),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.size(22.dp)
                    )
                }

                // ── Título + ícono de edición ─────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Checkbox de completado
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = state.title.ifBlank { stringResource(Res.string.edit_task_placeholder_title) },
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (state.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.description,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Botón editar título
                    IconButton(onClick = { viewModel.showTitleDialog() }) {
                        Icon(
                            painter = painterResource(Res.drawable.edit),
                            contentDescription = stringResource(Res.string.edit_task_cd_edit_title),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                // ── Campos de edición ─────────────────────────────────
                EditTaskFieldRow(
                    iconRes = Res.drawable.clock,
                    label = stringResource(Res.string.time_picker_title),
                    value = formatTaskTimeText(state.selectedDate, state.selectedTime),
                    onClick = { viewModel.showDatePicker() }
                )

                EditTaskFieldRow(
                    iconRes = Res.drawable.tag,
                    label = stringResource(Res.string.add_task_cd_tag),
                    valueContent = {
                        val category = state.categories.find { it.id == state.selectedCategoryId }
                        if (category != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        category.colorHex?.let { Color(it) }
                                            ?: MaterialTheme.colorScheme.primary
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = category.name,
                                    color = Color.Black,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            EditTaskChip(text = stringResource(Res.string.category_choose))
                        }
                    },
                    onClick = { viewModel.showCategoryPicker() }
                )

                EditTaskFieldRow(
                    iconRes = Res.drawable.flag,
                    label = stringResource(Res.string.edit_task_cd_priority),
                    value = when (state.selectedPriority) {
                        1 -> "Default"
                        else -> stringResource(Res.string.priority_cd_value, state.selectedPriority)
                    },
                    onClick = { viewModel.showPriorityPicker() }
                )

                // Sub-Task (solo visual por ahora)
                EditTaskFieldRow(
                    iconRes = Res.drawable.subtask,
                    label = "Sub - Task",
                    valueContent = {
                        EditTaskChip(text = "Add Sub-Task")
                    },
                    onClick = { /* TODO: sub-tasks */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Eliminar tarea ────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showDeleteConfirmDialog() }
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.trash),
                        contentDescription = stringResource(Res.string.edit_task_cd_delete),
                        tint = Color(0xFFFF4444),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(Res.string.edit_task_cd_delete),
                        color = Color(0xFFFF4444),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // ── Botón Guardar ─────────────────────────────────────
                state.saveError?.let { error ->
                    Text(
                        text = error,
                        color = Color(0xFFFF4444),
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Button(
                    onClick = { viewModel.saveTask() },
                    enabled = !state.isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .height(52.dp)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.common_edit),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    // ── Modales ───────────────────────────────────────────────────────────

    // Dialog para editar el título (como se ve en la imagen)
    if (state.isTitleDialogVisible) {
        EditTitleDialog(
            currentTitle = state.titleDialogDraft,
            description = state.description,
            onTitleChange = { viewModel.onTitleDialogDraftChange(it) },
            onCancel = { viewModel.hideTitleDialog() },
            onConfirm = { viewModel.confirmTitleEdit() }
        )
    }

    // DatePicker
    if (state.isDatePickerVisible) {
        DatePickerModal(
            initialDate = state.selectedDate,
            onDateSelected = { viewModel.onDateSelected(it) },
            onDismiss = { viewModel.hideDatePicker() }
        )
    }

    // TimePicker
    if (state.isTimePickerVisible) {
        TimePickerModal(
            initialTime = state.selectedTime,
            onTimeSelected = { viewModel.onTimeSelected(it) },
            onDismiss = { viewModel.hideTimePicker() }
        )
    }

    // PriorityPicker
    if (state.isPriorityPickerVisible) {
        PriorityPickerModal(
            currentPriority = state.selectedPriority,
            onPrioritySelected = { viewModel.onPrioritySelected(it) },
            onDismiss = { viewModel.hidePriorityPicker() }
        )
    }

    // CategoryPicker
    if (state.isCategoryPickerVisible) {
        EditTaskCategoryPickerModal(
            categories = state.categories,
            selectedCategoryId = state.selectedCategoryId,
            onCategorySelected = { viewModel.onCategorySelected(it) },
            onDismiss = { viewModel.hideCategoryPicker() }
        )
    }

    // Confirmar eliminación
    if (state.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteConfirmDialog() },
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = stringResource(Res.string.edit_task_cd_delete),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(Res.string.edit_task_delete_confirm) + " \"${state.title}\"?",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteTask() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444))
                ) {
                    Text(stringResource(Res.string.common_delete), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDeleteConfirmDialog() }) {
                    Text(stringResource(Res.string.common_cancel), color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

// ── Helpers de UI ─────────────────────────────────────────────────────────────

@Composable
private fun EditTaskFieldRow(
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    label: String,
    value: String? = null,
    valueContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        if (valueContent != null) {
            valueContent()
        } else if (value != null) {
            EditTaskChip(text = value)
        }
    }
}

@Composable
private fun EditTaskChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }
}

private fun formatTaskTimeText(
    taskDate: LocalDate?,
    taskTime: LocalTime?
): String? {
    if (taskDate == null || taskTime == null) return null
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    val tomorrow = today.plus(1, DateTimeUnit.DAY)
    val dateText = when (taskDate) {
        today -> "Today"
        tomorrow -> "Tomorrow"
        else -> {
            val monthName = taskDate.month.name
                .lowercase().replaceFirstChar { it.uppercase() }
            "$monthName ${taskDate.dayOfMonth}"
        }
    }
    val hour = taskTime.hour.toString().padStart(2, '0')
    val minute = taskTime.minute.toString().padStart(2, '0')
    return "$dateText At $hour:$minute"
}
