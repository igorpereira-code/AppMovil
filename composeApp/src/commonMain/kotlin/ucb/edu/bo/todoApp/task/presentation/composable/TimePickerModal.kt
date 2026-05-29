package ucb.edu.bo.todoApp.task.presentation.composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    initialTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialTime?.hour ?: 12,
        initialMinute = initialTime?.minute ?: 0,
        is24Hour = false // Fuerza el formato AM/PM como en tu diseño
    )

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(color = SurfaceDark, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose Time",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            TimePicker(
                state = state,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color(0xFF272727),
                    clockDialSelectedContentColor = Color.White,
                    clockDialUnselectedContentColor = Color.LightGray,
                    selectorColor = PrimaryPurple,
                    containerColor = SurfaceDark,
                    periodSelectorBorderColor = PrimaryPurple,
                    periodSelectorSelectedContainerColor = PrimaryPurple,
                    periodSelectorSelectedContentColor = Color.White,
                    periodSelectorUnselectedContentColor = Color.LightGray,
                    timeSelectorSelectedContainerColor = PrimaryPurple,
                    timeSelectorSelectedContentColor = Color.White,
                    timeSelectorUnselectedContainerColor = Color(0xFF272727),
                    timeSelectorUnselectedContentColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel", color = PrimaryPurple)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        val selectedTime = LocalTime(state.hour, state.minute)
                        onTimeSelected(selectedTime)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save", color = Color.White)
                }
            }
        }
    }
}