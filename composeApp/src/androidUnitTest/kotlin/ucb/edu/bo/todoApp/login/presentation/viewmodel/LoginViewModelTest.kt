package ucb.edu.bo.todoApp.login.presentation

import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertTrue
import ucb.edu.bo.fakes.FakeAuthRepository
import ucb.edu.bo.todoApp.login.domain.usecase.LoginUseCase
import ucb.edu.bo.todoApp.login.presentation.state.LoginState
import ucb.edu.bo.todoApp.login.presentation.viewmodel.LoginViewModel
import org.junit.Rule
import ucb.edu.bo.MainDispatcherRule

class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun login_success_updates_state_to_success() = runTest {
        val fakeRepository = FakeAuthRepository()
        val useCase = LoginUseCase(fakeRepository)
        val viewModel = LoginViewModel(useCase)

        viewModel.login(
            email = "igor@test.com",
            password = "123456"
        )

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        println(viewModel.state.value)

        assertTrue(viewModel.state.value is LoginState.Success)
    }

    @Test
    fun login_failure_updates_state_to_error() = runTest {
        val fakeRepository = FakeAuthRepository()

        fakeRepository.loginResult =
            Result.failure(Exception("Login failed"))

        val useCase = LoginUseCase(fakeRepository)
        val viewModel = LoginViewModel(useCase)

        viewModel.login(
            email = "igor@test.com",
            password = "123456"
        )

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value is LoginState.Error)
    }
}