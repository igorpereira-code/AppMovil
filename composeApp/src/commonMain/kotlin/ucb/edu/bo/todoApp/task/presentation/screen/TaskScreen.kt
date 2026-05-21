package ucb.edu.bo.todoApp.task.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appmovil.composeapp.generated.resources.Res
import appmovil.composeapp.generated.resources.add_image
import appmovil.composeapp.generated.resources.calendar
import appmovil.composeapp.generated.resources.checklist_rafiki_1
import appmovil.composeapp.generated.resources.flag
import appmovil.composeapp.generated.resources.home_2
import appmovil.composeapp.generated.resources.send
import appmovil.composeapp.generated.resources.sort_image
import appmovil.composeapp.generated.resources.tag
import appmovil.composeapp.generated.resources.timer
import appmovil.composeapp.generated.resources.user
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

// Color primario morado de la app
private val PrimaryPurple = Color(0xFF8687E7)
private val BackgroundDark = Color(0xFF121212)
private val SurfaceDark = Color(0xFF1D1D1D)
private val BottomSheetDark = Color(0xFF272727)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // ── Top Bar ──────────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono menú hamburguesa
            Icon(
                painter = painterResource(Res.drawable.sort_image),
                contentDescription = "Menú",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Index",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            // Avatar de perfil
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF444444)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.user),
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ── Contenido principal ──────────────────────────────────────────────────
        if (state.isLoading) {
            CircularProgressIndicator(
                color = PrimaryPurple,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (state.tasks.isEmpty()) {
            EmptyTasksContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 72.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 72.dp, bottom = 80.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.tasks, key = { it.id }) { task ->
                    TaskItem(
                        title = task.title,
                        description = task.description,
                        isCompleted = task.isCompleted,
                        onToggle = { viewModel.toggleTask(task.id, !task.isCompleted) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }

        // ── Bottom Nav Bar ───────────────────────────────────────────────────────
        BottomNavBar(
            onAddClick = { viewModel.showAddTaskSheet() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // ── Add Task Bottom Sheet ────────────────────────────────────────────────────
    if (state.isAddTaskSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideAddTaskSheet() },
            sheetState = sheetState,
            containerColor = BottomSheetDark,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            AddTaskSheetContent(
                title = state.newTaskTitle,
                description = state.newTaskDescription,
                isSaving = state.isSaving,
                errorMessage = state.saveError,
                onTitleChange = { viewModel.onTitleChange(it) },
                onDescriptionChange = { viewModel.onDescriptionChange(it) },
                onSend = { viewModel.saveTask() }
            )
        }
    }
}

// ── Empty state ──────────────────────────────────────────────────────────────────

@Composable
private fun EmptyTasksContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ilustración central (coloca tu imagen en res/drawable/img_empty_tasks.png)
        Icon(
            painter = painterResource(Res.drawable.checklist_rafiki_1),
            contentDescription = null,
            tint = Color(0xFF888888),
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "¿Qué quieres hacer hoy?",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Presiona + para agregar tus tareas",
            color = Color(0xFF888888),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ── Task item card ───────────────────────────────────────────────────────────────

@Composable
private fun TaskItem(
    title: String,
    description: String,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox circular personalizado
            IconButton(onClick = onToggle, modifier = Modifier.size(28.dp)) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) PrimaryPurple else Color.Transparent)
                        .then(
                            if (!isCompleted) Modifier.background(
                                Color.Transparent
                            ) else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        RadioButton(
                            selected = isCompleted,
                            onClick = onToggle,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = PrimaryPurple,
                                unselectedColor = Color(0xFF666666)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isCompleted) Color(0xFF888888) else Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        color = Color(0xFF888888),
                        fontSize = 12.sp,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            // Botón eliminar
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    painter = painterResource(Res.drawable.add_image),
                    contentDescription = "Eliminar",
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ── Bottom Nav Bar ───────────────────────────────────────────────────────────────

@Composable
private fun BottomNavBar(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1D1D1D))
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Index (Home)
            NavItem(
                iconRes = Res.drawable.home_2,
                label = "Index",
                isSelected = true
            )
            // Calendar
            NavItem(
                iconRes = Res.drawable.calendar,
                label = "Calendario",
                isSelected = false
            )

            // FAB central (botón +)
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PrimaryPurple,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_image),
                    contentDescription = "Agregar tarea",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Focus
            NavItem(
                iconRes = Res.drawable.timer,
                label = "Focus",
                isSelected = false
            )
            // Profile
            NavItem(
                iconRes = Res.drawable.user,
                label = "Perfil",
                isSelected = false
            )
        }
    }
}

@Composable
private fun NavItem(iconRes: DrawableResource, label: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.home_2),
            contentDescription = label,
            tint = if (isSelected) PrimaryPurple else Color(0xFF888888),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) PrimaryPurple else Color(0xFF888888),
            fontSize = 10.sp
        )
    }
}

// ── Add Task Bottom Sheet content ────────────────────────────────────────────────

@Composable
private fun AddTaskSheetContent(
    title: String,
    description: String,
    isSaving: Boolean,
    errorMessage: String?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = "Agregar Tarea",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo Título
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = { Text("Título de la tarea", color = Color(0xFF888888)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = Color(0xFF444444),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = PrimaryPurple,
                focusedContainerColor = Color(0xFF1D1D1D),
                unfocusedContainerColor = Color(0xFF1D1D1D)
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo Descripción
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = { Text("Descripción", color = Color(0xFF888888)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = Color(0xFF444444),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = PrimaryPurple,
                focusedContainerColor = Color(0xFF1D1D1D),
                unfocusedContainerColor = Color(0xFF1D1D1D)
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Error
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Fila de iconos de acción + botón enviar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Iconos de acción (timer, tag, flag) — decorativos por ahora
            // Reemplaza estos con tus drawables personalizados en res/drawable
            Icon(
                painter = painterResource(Res.drawable.timer),
                contentDescription = "Timer",
                tint = Color(0xFF888888),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(Res.drawable.tag),
                contentDescription = "Etiqueta",
                tint = Color(0xFF888888),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(Res.drawable.flag),
                contentDescription = "Prioridad",
                tint = Color(0xFF888888),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón enviar
            if (isSaving) {
                CircularProgressIndicator(
                    color = PrimaryPurple,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(
                    onClick = onSend,
                    enabled = title.isNotBlank(),
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (title.isNotBlank()) PrimaryPurple else Color(0xFF444444))
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.send),
                        contentDescription = "Guardar tarea",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}