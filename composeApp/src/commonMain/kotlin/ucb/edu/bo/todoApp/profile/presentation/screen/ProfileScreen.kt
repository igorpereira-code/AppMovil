package ucb.edu.bo.todoApp.profile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.Screen
import ucb.edu.bo.todoApp.profile.presentation.viewmodel.ProfileViewModel
import ucb.edu.bo.todoApp.task.presentation.composable.BottomNavBar
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF363636)
val PrimaryPurple = Color(0xFF8687E7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel(),
    taskViewModel: TaskViewModel,
    onNavigateToSettings: () -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val taskState by taskViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // CÁLCULO DE TAREAS REALES
    val tasksDone = taskState.tasks.count { it.isCompleted }
    val tasksLeft = taskState.tasks.count { !it.isCompleted }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            Text(
                text = "Perfil",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(85.dp).clip(CircleShape).background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.userName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ESTADÍSTICAS REALES
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(modifier = Modifier.weight(1f), text = "$tasksLeft Tareas pendientes")
                StatCard(modifier = Modifier.weight(1f), text = "$tasksDone Tareas terminadas")
            }

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item { MenuSectionTitle("Configuración") }
                item { ProfileMenuItem(icon = Icons.Outlined.Settings, text = "Configuración de la app", onClick = onNavigateToSettings) }

                item { MenuSectionTitle("Cuenta") }
                item { ProfileMenuItem(icon = Icons.Outlined.PersonOutline, text = "Cambiar nombre de cuenta", onClick = { viewModel.toggleNameDialog(true) }) }
                item { ProfileMenuItem(icon = Icons.Outlined.Lock, text = "Cambiar contraseña", onClick = { viewModel.togglePasswordDialog(true) }) }
                item { ProfileMenuItem(icon = Icons.Outlined.CameraAlt, text = "Cambiar imagen de perfil", onClick = { viewModel.toggleAvatarDialog(true) }) }

                item { MenuSectionTitle("Uptodo") }
                item { ProfileMenuItem(icon = Icons.Outlined.Info, text = "Sobre nosotros", onClick = { viewModel.toggleAboutDialog(true) }) }
                item { ProfileMenuItem(icon = Icons.Outlined.LiveHelp, text = "Preguntas frecuentes", onClick = { viewModel.toggleFaqDialog(true) }) }
                item { ProfileMenuItem(icon = Icons.AutoMirrored.Outlined.HelpOutline, text = "Ayuda y comentarios", onClick = { viewModel.toggleHelpDialog(true) }) }
                item { ProfileMenuItem(icon = Icons.Outlined.ThumbUp, text = "Apóyanos", onClick = { viewModel.toggleSupportDialog(true) }) }

                item {
                    ProfileMenuItem(icon = Icons.AutoMirrored.Outlined.Logout, text = "Cerrar sesión", isDestructive = true, onClick = onLogoutSuccess)
                }
            }
        }

        BottomNavBar(
            currentRoute = "Profile",
            onHomeClick = { navController.navigate(Screen.Task.route) { popUpTo(Screen.Task.route) { inclusive = true } } },
            onCalendarClick = { navController.navigate(Screen.Calendar.route) },
            onFocusClick = { navController.navigate(Screen.Focus.route) },
            onProfileClick = { },
            onAddClick = { },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // ── MODALES REDISEÑADOS SEGÚN MOCKUP ──

    if (state.showNameDialog) {
        ChangeNameDialog(
            currentName = state.userName,
            onDismiss = { viewModel.toggleNameDialog(false) },
            onSave = { viewModel.updateName(it) }
        )
    }

    if (state.showPasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { viewModel.togglePasswordDialog(false) },
            onSave = { old, new -> viewModel.updatePassword(new) }
        )
    }

    // Menú de Imagen (Bottom Sheet Exacto al mockup)
    if (state.showAvatarDialog) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleAvatarDialog(false) },
            sheetState = sheetState,
            containerColor = SurfaceDark,
            dragHandle = null // Quita la barrita de arriba para que se vea como en tu diseño
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                Text(
                    text = "Cambiar imagen de perfil",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), color = Color.Gray)

                BottomSheetMenuItem("Tomar foto") { viewModel.toggleAvatarDialog(false) }
                BottomSheetMenuItem("Importar de la galería") { viewModel.toggleAvatarDialog(false) }
                BottomSheetMenuItem("Importar de Google Drive") { viewModel.toggleAvatarDialog(false) }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Modales de Info
    if (state.showAboutDialog) {
        InfoDialog("Sobre Nosotros", "Uptodo está diseñada para ayudarte a organizar tu vida de manera eficiente y moderna. ¡Gracias por usar nuestra app!", { viewModel.toggleAboutDialog(false) })
    }
    if (state.showFaqDialog) {
        InfoDialog("Preguntas Frecuentes", "¿Cómo borro una tarea? \nDesliza o haz clic en ella.\n\n¿El modo Focus funciona en segundo plano? \nPor el momento debes mantener la app abierta.", { viewModel.toggleFaqDialog(false) })
    }
    if (state.showHelpDialog) {
        InfoDialog("Ayuda y Comentarios", "Si encontraste un error o tienes alguna sugerencia, por favor escríbenos a soporte@uptodo.bo. ¡Valoramos tu feedback!", { viewModel.toggleHelpDialog(false) })
    }
    if (state.showSupportDialog) {
        InfoDialog("Apóyanos", "Si te gusta Uptodo, no olvides dejarnos 5 estrellas en la tienda. ¡Cada pequeño apoyo nos ayuda a seguir mejorando!", { viewModel.toggleSupportDialog(false) })
    }
}

// ── COMPONENTES REUTILIZABLES ──

@Composable
fun StatCard(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier.background(Color(0xFF363636), RoundedCornerShape(8.dp)).padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun MenuSectionTitle(title: String) {
    Text(text = title, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp))
}

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String, isDestructive: Boolean = false, onClick: () -> Unit) {
    val color = if (isDestructive) Color.Red else Color.White
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = color, fontSize = 16.sp, modifier = Modifier.weight(1f))
        if (!isDestructive) { Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color.White) }
    }
}

@Composable
fun BottomSheetMenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text, color = Color.White, fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 24.dp, vertical = 16.dp)
    )
}

// ── DIÁLOGOS REDISEÑADOS ──

@Composable
fun ChangeNameDialog(currentName: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var name by remember { mutableStateOf(currentName) }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Cambiar nombre de cuenta", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancelar", color = PrimaryPurple, fontSize = 16.sp)
                    }
                    Button(
                        onClick = { onSave(name) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Editar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Cambiar contraseña", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray)

                Text("Contraseña actual", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = oldPass,
                    onValueChange = { oldPass = it },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color.Gray, unfocusedBorderColor = Color.Gray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Nueva contraseña", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color.Gray, unfocusedBorderColor = Color.Gray)
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancelar", color = PrimaryPurple, fontSize = 16.sp)
                    }
                    Button(
                        onClick = { onSave(oldPass, newPass) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Editar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoDialog(title: String, content: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = title, color = PrimaryPurple, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = content, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End), colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple), shape = RoundedCornerShape(4.dp)) {
                    Text("Entendido")
                }
            }
        }
    }
}