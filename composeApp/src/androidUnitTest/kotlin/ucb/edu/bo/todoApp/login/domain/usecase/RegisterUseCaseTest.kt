package ucb.edu.bo.todoApp.login.domain.usecase

import kotlinx.coroutines.test.runTest
import ucb.edu.bo.fakes.FakeAuthRepository
import kotlin.test.Test
import kotlin.test.assertTrue

class RegisterUseCaseTest {

    @Test
    fun register_success_returns_user() = runTest {

        val fakeRepository = FakeAuthRepository()

        val registerUseCase = RegisterUseCase(
            fakeRepository
        )

        val result = registerUseCase(
            "Igor",
            "igor@test.com",
            "123456"
        )

        assertTrue(result.isSuccess)
    }

    @Test
    fun register_failure_returns_error() = runTest {

        val fakeRepository = FakeAuthRepository()

        fakeRepository.registerResult =
            Result.failure(
                Exception("Error al registrar")
            )

        val registerUseCase = RegisterUseCase(
            fakeRepository
        )

        val result = registerUseCase(
            "Igor",
            "igor@test.com",
            "123456"
        )

        assertTrue(result.isFailure)
    }
}