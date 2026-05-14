package ucb.edu.bo.app.home.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.search_normal
import org.jetbrains.compose.resources.painterResource
import ucb.edu.bo.app.home.domain.model.Task

@Composable
fun TaskList(tasks: List<Task>) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            OutlinedTextField(
                value = "", onValueChange = {},
                placeholder = { Text("Search for your task...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { 
                    Icon(
                        painter = painterResource(Res.drawable.search_normal), 
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    ) 
                }
            )
            Spacer(Modifier.height(16.dp))
        }
        items(tasks) { task ->
            TaskItem(task)
            Spacer(Modifier.height(12.dp))
        }
    }
}
