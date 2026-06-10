package ucb.edu.bo.todoApp.login.domain.usecase

import kotlinx.coroutines.test.runTest
import ucb.edu.bo.fakes.FakeAuthRepository
import kotlin.test.Test
import kotlin.test.assertTrue

class LoginUseCaseTest {

    @Test
    fun login_success_returns_user() = runTest {

        val fakeRepository = FakeAuthRepository()

        val loginUseCase = LoginUseCase(
            fakeRepository
        )

        val result = loginUseCase(
            "igor@test.com",
            "123456"
        )

        assertTrue(result.isSuccess)
    }

    @Test
    fun login_failure_returns_error() = runTest {

        val fakeRepository = FakeAuthRepository()

        fakeRepository.loginResult =
            Result.failure(
                Exception("Credenciales incorrectas")
            )

        val loginUseCase = LoginUseCase(
            fakeRepository
        )

        val result = loginUseCase(
            "igor@test.com",
            "123456"
        )

        assertTrue(result.isFailure)
    }
}