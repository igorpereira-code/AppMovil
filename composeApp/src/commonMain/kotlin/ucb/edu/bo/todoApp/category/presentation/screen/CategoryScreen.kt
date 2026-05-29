// ucb.edu.bo.todoApp.category.presentation.screen.CategoryScreen.kt
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
import org.koin.compose.viewmodel.koinViewModel
import ucb.edu.bo.todoApp.category.presentation.composable.CategoryGridItem
import ucb.edu.bo.todoApp.category.presentation.viewmodel.CategoryViewModel

val SurfaceDark = Color(0xFF1D1D1D)
val PrimaryPurple = Color(0xFF8687E7)

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = koinViewModel(),
    onCategorySelected: (Int) -> Unit // Para retornar la categoría a la pantalla de Task
) {
    val state by viewModel.state.collectAsState()

    // Controla qué pantalla mostrar basándose en el estado
    if (state.isCreatingNew) {
        CreateCategoryView(
            name = state.newCategoryName,
            selectedColor = state.selectedColor,
            errorMessage = state.saveError,
            onNameChange = { viewModel.updateName(it) },
            onColorSelect = { viewModel.selectColor(it) },
            onCancel = { viewModel.toggleCreateMode(false) },
            onSave = { viewModel.saveCategory() }
        )
    } else {
        ChooseCategoryView(
            categories = state.categories,
            onCreateNew = { viewModel.toggleCreateMode(true) },
            onSelect = onCategorySelected
        )
    }
}

@Composable
fun ChooseCategoryView(
    categories: List<ucb.edu.bo.todoApp.category.domain.model.CategoryModel>,
    onCreateNew: () -> Unit,
    onSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Choose Category",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(300.dp)
        ) {
            items(categories) { category ->
                CategoryGridItem(
                    name = category.name,
                    colorHex = category.colorHex,
                    onClick = { onSelect(category.id) }
                )
            }
            item {
                CategoryGridItem(
                    name = "Create New",
                    colorHex = 0xFF00FFCC, // Verde de la imagen
                    onClick = onCreateNew
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* Acción por defecto si es necesario */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add Category", modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryView(
    name: String,
    selectedColor: Long,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    // Colores basados en tu diseño
    val palette = listOf(0xFFCCFF90, 0xFFFF8A80, 0xFF80D8FF, 0xFFFFD180, 0xFFB388FF, 0xFFFF80AB, 0xFF84FFFF, 0xFFFFFF8D)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text("Create new category", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Category name :", color = Color.LightGray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = { Text("Category name", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = Color(0xFF444444),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Category icon :", color = Color.LightGray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .background(Color(0xFF333333), RoundedCornerShape(8.dp))
                .clickable { /* Abrir selector de iconos */ }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text("Choose icon from library", color = Color.White, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Category color :", color = Color.LightGray, fontSize = 14.sp)
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
                            color = if (selectedColor == colorHex) Color.White else Color.Transparent,
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
            Text(text = it, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancel", color = PrimaryPurple)
            }
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Create Category", modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}