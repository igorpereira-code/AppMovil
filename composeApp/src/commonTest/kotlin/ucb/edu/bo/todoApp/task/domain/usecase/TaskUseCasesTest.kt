package ucb.edu.bo.todoApp.task.domain.usecase

import ucb.edu.bo.fakes.FakeTaskRepository
import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetAllTasksUseCaseTest {
    private lateinit var fakeRepo: FakeTaskRepository
    private lateinit var useCase: GetAllTasksUseCase

    @BeforeTest fun setUp() {
        fakeRepo = FakeTaskRepository()
        useCase = GetAllTasksUseCase(fakeRepo)
    }

    @Test
    fun `retorna lista vacia cuando no hay tareas`() = runTest {
        assertTrue(useCase().isEmpty())
    }

    @Test
    fun `retorna todas las tareas existentes`() = runTest {
        fakeRepo.setTasks(listOf(
            TaskModel(id = 1, title = "T1"), TaskModel(id = 2, title = "T2")
        ))
        assertEquals(2, useCase().size)
    }

    @Test
    fun `retorna tareas en el mismo orden del repositorio`() = runTest {
        fakeRepo.setTasks(listOf(
            TaskModel(id = 1, title = "Primera"), TaskModel(id = 2, title = "Segunda")
        ))
        val tasks = useCase()
        assertEquals("Primera", tasks[0].title)
        assertEquals("Segunda", tasks[1].title)
    }
}

class DeleteTaskUseCaseTest {
    private lateinit var fakeRepo: FakeTaskRepository
    private lateinit var useCase: DeleteTaskUseCase

    @BeforeTest fun setUp() {
        fakeRepo = FakeTaskRepository()
        useCase = DeleteTaskUseCase(fakeRepo)
    }

    @Test
    fun `eliminar tarea existente retorna exito`() = runTest {
        fakeRepo.setTasks(listOf(TaskModel(id = 1, title = "Para eliminar")))
        assertTrue(useCase(1).isSuccess)
    }

    @Test
    fun `tarea eliminada ya no existe en el repositorio`() = runTest {
        fakeRepo.setTasks(listOf(
            TaskModel(id = 1, title = "Mantener"), TaskModel(id = 2, title = "Eliminar")
        ))
        useCase(2)
        assertEquals(1, fakeRepo.tasksSnapshot.size)
        assertEquals("Mantener", fakeRepo.tasksSnapshot.first().title)
    }

    @Test
    fun `eliminar cuando repositorio falla retorna fallo`() = runTest {
        fakeRepo.shouldFail = true
        assertTrue(useCase(1).isFailure)
    }
}

class ToggleTaskUseCaseTest {
    private lateinit var fakeRepo: FakeTaskRepository
    private lateinit var useCase: ToggleTaskUseCase

    @BeforeTest fun setUp() {
        fakeRepo = FakeTaskRepository()
        useCase = ToggleTaskUseCase(fakeRepo)
    }

    @Test
    fun `marcar tarea como completada retorna exito`() = runTest {
        fakeRepo.setTasks(listOf(TaskModel(id = 1, title = "Tarea", isCompleted = false)))
        assertTrue(useCase(1, true).isSuccess)
    }

    @Test
    fun `tarea marcada como completada cambia su estado`() = runTest {
        fakeRepo.setTasks(listOf(TaskModel(id = 1, title = "Tarea", isCompleted = false)))
        useCase(1, true)
        assertTrue(fakeRepo.tasksSnapshot.first().isCompleted)
    }

    @Test
    fun `tarea descompletada cambia su estado a false`() = runTest {
        fakeRepo.setTasks(listOf(TaskModel(id = 1, title = "Tarea", isCompleted = true)))
        useCase(1, false)
        assertFalse(fakeRepo.tasksSnapshot.first().isCompleted)
    }

    @Test
    fun `toggle falla cuando repositorio falla`() = runTest {
        fakeRepo.shouldFail = true
        assertTrue(useCase(1, true).isFailure)
    }
}