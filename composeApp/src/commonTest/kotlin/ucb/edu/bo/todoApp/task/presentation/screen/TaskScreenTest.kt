package ucb.edu.bo.todoApp.task.presentation.screen

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.navigation.compose.rememberNavController
import ucb.edu.bo.fakes.FakeCategoryRepository
import ucb.edu.bo.fakes.FakeTaskRepository
import ucb.edu.bo.todoApp.category.domain.usecase.GetAllCategoriesUseCase
import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import ucb.edu.bo.todoApp.task.domain.usecase.CreateTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.DeleteTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.GetAllTasksUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.ToggleTaskUseCase
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class TaskScreenTest {

    private fun buildTestViewModel(initialTasks: List<TaskModel> = emptyList()): TaskViewModel {
        val taskRepo = FakeTaskRepository().also { it.setTasks(initialTasks) }
        val categoryRepo = FakeCategoryRepository()

        return TaskViewModel(
            getAllTasksUseCase = GetAllTasksUseCase(taskRepo),
            createTaskUseCase = CreateTaskUseCase(taskRepo),
            deleteTaskUseCase = DeleteTaskUseCase(taskRepo),
            toggleTaskUseCase = ToggleTaskUseCase(taskRepo),
            getAllCategoriesUseCase = GetAllCategoriesUseCase(categoryRepo)
        )
    }

    @Test
    fun listaDeTareasMuestraTareaEspecifica() = runComposeUiTest {
        // 1. Preparar datos
        val tituloTarea = "Revisar código de Isabel"
        val tareaPrueba = TaskModel(id = 101, title = tituloTarea, isCompleted = false)
        val testViewModel = buildTestViewModel(initialTasks = listOf(tareaPrueba))
        
        // 2. Configurar el contenido de la prueba
        setContent {
            // Usamos el NavController real de Compose, lo cual es más simple que usar MockK
            val navController = rememberNavController()

            TaskScreen(
                viewModel = testViewModel,
                navController = navController
            )
        }

        // 3. Verificación
        onNodeWithText(tituloTarea).assertIsDisplayed()
    }
}
