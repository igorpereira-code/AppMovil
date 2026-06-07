package ucb.edu.bo.todoApp.category.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import appmovil.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.todoApp.category.domain.model.CategoryModel
import ucb.edu.bo.todoApp.category.presentation.composable.CategoryGridItem
import ucb.edu.bo.todoApp.category.presentation.composable.getCategoryIconResource
import ucb.edu.bo.todoApp.category.presentation.viewmodel.CategoryViewModel

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = koinViewModel(),
    onCategorySelected: (Int) -> Unit,
    onClose: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isCreatingNew) {
            CreateCategoryView(
                name = state.newCategoryName,
                selectedColor = state.selectedColor,
                selectedIcon = state.selectedIcon,
                errorMessage = state.saveError,
                onNameChange = { viewModel.updateName(it) },
                onColorSelect = { viewModel.selectColor(it) },
                onIconSelect = { viewModel.selectIcon(it) },
                onCancel = { viewModel.toggleCreateMode(false) },
                onSave = { viewModel.saveCategory() }
            )
        } else {
            ChooseCategoryView(
                categories = state.categories,
                onCreateNew = { viewModel.toggleCreateMode(true) },
                onSelect = onCategorySelected,
                onBack = onClose
            )
        }
    }
}

@Composable
fun ChooseCategoryView(
    categories: List<CategoryModel>,
    onCreateNew: () -> Unit,
    onSelect: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text("<", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(Res.string.category_choose),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1.5f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(400.dp)
        ) {
            items(categories) { category ->
                CategoryGridItem(
                    name = category.name,
                    colorHex = category.colorHex,
                    iconName = category.iconResName,
                    onClick = { onSelect(category.id) }
                )
            }
            item {
                CategoryGridItem(
                    name = stringResource(Res.string.add_new_category),
                    colorHex = 0xFF00FFCC,
                    iconName = "add_image",
                    onClick = onCreateNew
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.category_title_add),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryView(
    name: String,
    selectedColor: Long,
    selectedIcon: String,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onIconSelect: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    val palette = listOf(0xFFCCFF90, 0xFFFF8A80, 0xFF80D8FF, 0xFFFFD180, 0xFFB388FF, 0xFFFF80AB, 0xFF84FFFF, 0xFFFFFF8D)

    var showIconDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(Res.string.category_title_add),
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.category_label_name),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = { Text(stringResource(Res.string.category_label_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.category_label_icon),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (selectedIcon.isEmpty()) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .clickable { showIconDialog = true }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.profile_image_import_gallery),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .clickable { showIconDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(getCategoryIconResource(selectedIcon)),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.category_label_color),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            palette.forEach { colorHex ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(colorHex))
                        .clickable { onColorSelect(colorHex) }
                        .border(
                            width = if (selectedColor == colorHex) 3.dp else 0.dp,
                            color = if (selectedColor == colorHex) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedColor == colorHex) {
                        Text("✓", color = Color.Black)
                    }
                }
            }
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text(stringResource(Res.string.common_cancel), color = MaterialTheme.colorScheme.primary)
            }
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(Res.string.category_title_add), modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }

    if (showIconDialog) {
        IconSelectionDialog(
            onIconSelected = {
                onIconSelect(it)
                showIconDialog = false
            },
            onDismiss = { showIconDialog = false }
        )
    }
}

@Composable
fun IconSelectionDialog(onIconSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val iconList = listOf("school", "home", "social", "game", "exercise", "food", "heart", "cake")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = stringResource(Res.string.category_title_select_icon),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(iconList) { iconName ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { onIconSelected(iconName) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(getCategoryIconResource(iconName)),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
