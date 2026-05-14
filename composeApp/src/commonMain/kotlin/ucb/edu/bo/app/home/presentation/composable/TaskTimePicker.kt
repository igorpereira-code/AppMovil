package ucb.edu.bo.app.home.presentation.composable

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTimePicker(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = Color(0xFF363636),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Choose Time",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Divider(color = Color.Gray.copy(alpha = 0.5f))
                
                Spacer(modifier = Modifier.height(24.dp))

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color(0xFF1D1D1D),
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Color.Gray,
                        selectorColor = Color(0xFF8875FF),
                        periodSelectorBorderColor = Color(0xFF8875FF),
                        periodSelectorSelectedContainerColor = Color(0xFF8875FF),
                        periodSelectorUnselectedContainerColor = Color.Transparent,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = Color.Gray,
                        timeSelectorSelectedContainerColor = Color(0xFF8875FF),
                        timeSelectorUnselectedContainerColor = Color(0xFF1D1D1D),
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF8875FF))
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            val time = "${timePickerState.hour}:${timePickerState.minute}"
                            onTimeSelected(time)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8875FF)),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
