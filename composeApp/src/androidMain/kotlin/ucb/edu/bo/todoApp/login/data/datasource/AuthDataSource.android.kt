package ucb.edu.bo.todoApp.login.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import ucb.edu.bo.todoApp.login.domain.model.User

actual class AuthDataSource actual constructor() {

    private val auth = FirebaseAuth.getInstance()

    actual suspend fun login(email: String, password: String): Result<User> {
        // Validaciones locales
        if (email.isBlank()) return Result.failure(Exception("El correo no puede estar vacío"))
        if (password.isBlank()) return Result.failure(Exception("La contraseña no puede estar vacía"))

        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!
            Result.success(
                User(
                    uid = firebaseUser.uid,
                    username = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: ""
                )
            )
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("No existe una cuenta con este correo"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Correo o contraseña incorrectos"))
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión: ${e.message}"))
        }
    }

    actual suspend fun register(username: String, email: String, password: String): Result<User> {
        // Validaciones locales
        if (username.isBlank()) return Result.failure(Exception("El nombre de usuario no puede estar vacío"))
        if (email.isBlank()) return Result.failure(Exception("El correo no puede estar vacío"))
        if (!email.contains("@")) return Result.failure(Exception("El correo no es válido"))
        if (password.length < 6) return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))

        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!
            val profileUpdates = userProfileChangeRequest {
                displayName = username
            }
            firebaseUser.updateProfile(profileUpdates).await()
            Result.success(
                User(
                    uid = firebaseUser.uid,
                    username = username,
                    email = firebaseUser.email ?: ""
                )
            )
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(Exception("La contraseña es muy débil, usa al menos 6 caracteres"))
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Ya existe una cuenta con este correo"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("El formato del correo no es válido"))
        } catch (e: Exception) {
            Result.failure(Exception("Error al crear la cuenta: ${e.message}"))
        }
    }

    // ── NUEVAS FUNCIONES PARA EL PERFIL ──────────────────────────────────────

    actual suspend fun changePassword(newPassword: String): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("No hay sesión activa"))

        return try {
            user.updatePassword(newPassword).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error al cambiar contraseña"))
        }
    }

    actual suspend fun updateName(newName: String): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("No hay sesión activa"))

        return try {
            val profileUpdates = userProfileChangeRequest {
                displayName = newName
            }
            user.updateProfile(profileUpdates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error al actualizar nombre"))
        }
    }

    actual fun getCurrentUserName(): String {
        val user = auth.currentUser
        // Intenta sacar el nombre real. Si está vacío, usa lo que está antes del "@" en el correo.
        return user?.displayName?.takeIf { it.isNotBlank() }
            ?: user?.email?.substringBefore("@")
            ?: "Usuario"
    }

    // ¡AQUÍ ESTÁ LA FUNCIÓN QUE FALTABA PARA QUITAR EL ERROR!
    actual suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error al cerrar sesión"))
        }
    }
}