package ucb.edu.bo.app.home.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ucb.edu.bo.app.home.domain.model.Task

@Composable
fun TaskItem(task: Task) {
    Row(
        Modifier.fillMaxWidth().background(Color(0xFF363636), RoundedCornerShape(4.dp)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(18.dp).background(Color.Transparent, CircleShape).background(Color.Gray.copy(0.2f), CircleShape))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(task.title, color = Color.White, fontSize = 16.sp)
            if (task.time.isNotEmpty()) {
                Text(task.time, color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}
