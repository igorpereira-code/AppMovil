package ucb.edu.bo.todoApp.task.domain.usecase

import ucb.edu.bo.fakes.FakeTaskRepository
import ucb.edu.bo.todoApp.task.domain.model.TaskModel
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateTaskUseCaseTest {

    private lateinit var fakeRepo: FakeTaskRepository
    private lateinit var useCase: CreateTaskUseCase

    @BeforeTest
    fun setUp() {
        fakeRepo = FakeTaskRepository()
        useCase = CreateTaskUseCase(fakeRepo)
    }

    @Test
    fun `crear tarea con titulo valido retorna exito`() = runTest {
        val result = useCase(TaskModel(title = "Estudiar Kotlin"))
        assertTrue(result.isSuccess)
    }

    @Test
    fun `tarea creada se agrega al repositorio`() = runTest {
        useCase(TaskModel(title = "Preparar examen"))
        assertEquals(1, fakeRepo.tasksSnapshot.size)
    }

    @Test
    fun `crear tarea con titulo en blanco retorna fallo`() = runTest {
        val result = useCase(TaskModel(title = ""))
        assertTrue(result.isFailure)
    }

    @Test
    fun `crear tarea con titulo solo espacios retorna fallo`() = runTest {
        val result = useCase(TaskModel(title = "   "))
        assertTrue(result.isFailure)
    }

    @Test
    fun `mensaje de error al titulo vacio es correcto`() = runTest {
        val result = useCase(TaskModel(title = ""))
        assertEquals("El título no puede estar vacío", result.exceptionOrNull()?.message)
    }

    @Test
    fun `crear tarea cuando repositorio falla retorna fallo`() = runTest {
        fakeRepo.shouldFail = true
        val result = useCase(TaskModel(title = "Tarea valida"))
        assertTrue(result.isFailure)
    }

    @Test
    fun `crear multiples tareas las agrega todas`() = runTest {
        useCase(TaskModel(title = "Tarea 1"))
        useCase(TaskModel(title = "Tarea 2"))
        useCase(TaskModel(title = "Tarea 3"))
        assertEquals(3, fakeRepo.tasksSnapshot.size)
    }
}