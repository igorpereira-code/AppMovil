package ucb.edu.bo.todoApp.task.presentation.composable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    // Convertimos el LocalDate de tu estado MVI a milisegundos
    val initialMillis = initialDate?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = DatePickerDefaults.colors(
            containerColor = SurfaceDark,
        ),
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convertimos de vuelta a LocalDate
                        val selectedDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC)
                            .date
                        onDateSelected(selectedDate)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(Res.string.date_picker_button_choose), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.common_cancel), color = MaterialTheme.colorScheme.primary)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.primary,
                headlineContentColor = Color.White,
                weekdayContentColor = Color.LightGray,
                dayContentColor = Color.White,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                selectedDayContentColor = Color.White,
                todayContentColor = MaterialTheme.colorScheme.primary,
                todayDateBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}