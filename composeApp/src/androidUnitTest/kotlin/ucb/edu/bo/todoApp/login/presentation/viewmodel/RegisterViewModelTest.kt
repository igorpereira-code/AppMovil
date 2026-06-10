package ucb.edu.bo.todoApp.login.presentation

import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule
import ucb.edu.bo.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertTrue
import ucb.edu.bo.fakes.FakeAuthRepository
import ucb.edu.bo.todoApp.login.domain.usecase.RegisterUseCase
import ucb.edu.bo.todoApp.login.presentation.state.RegisterState
import ucb.edu.bo.todoApp.login.presentation.viewmodel.RegisterViewModel

class RegisterViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun register_success_updates_state_to_success() = runTest {
        val fakeRepository = FakeAuthRepository()

        val useCase = RegisterUseCase(fakeRepository)
        val viewModel = RegisterViewModel(useCase)

        viewModel.register(
            username = "Igor",
            email = "igor@test.com",
            password = "123456"
        )

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()


        assertTrue(viewModel.state.value is RegisterState.Success)
    }

    @Test
    fun register_failure_updates_state_to_error() = runTest {
        val fakeRepository = FakeAuthRepository()

        fakeRepository.registerResult =
            Result.failure(Exception("Register failed"))

        val useCase = RegisterUseCase(fakeRepository)
        val viewModel = RegisterViewModel(useCase)

        viewModel.register(
            username = "Igor",
            email = "igor@test.com",
            password = "123456"
        )

        advanceUntilIdle()

        println(viewModel.state.value)

        assertTrue(viewModel.state.value is RegisterState.Error)
    }
}