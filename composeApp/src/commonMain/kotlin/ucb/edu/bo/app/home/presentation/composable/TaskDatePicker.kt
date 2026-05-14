package ucb.edu.bo.app.home.presentation.composable

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        // For simplicity, converting to a basic string. 
                        // In a real app, use a proper date formatter.
                        onDateSelected("2022-02-09") 
                    } ?: onDateSelected("2022-02-09") // Default for demo if none selected
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8875FF))
            ) {
                Text("Choose Time")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF8875FF))
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFF363636),
            titleContentColor = Color.White,
            headlineContentColor = Color.White,
            selectedDayContainerColor = Color(0xFF8875FF),
            selectedDayContentColor = Color.White,
            todayContentColor = Color(0xFF8875FF),
            todayDateBorderColor = Color(0xFF8875FF),
            dayContentColor = Color.White,
            weekdayContentColor = Color.White
        )
    ) {
        DatePicker(state = datePickerState)
    }
}
