package ucb.edu.bo.formulario.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.formulario.domain.model.FormularioModel
import ucb.edu.bo.formulario.domain.usecase.SaveFormularioLocalUseCase

class FormularioAutoSaveWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val saveFormularioLocalUseCase: SaveFormularioLocalUseCase by inject()

    override suspend fun doWork(): Result {
        return try {
            val nombre = inputData.getString("nombre") ?: ""
            val mensaje = inputData.getString("mensaje") ?: ""

            if (nombre.isBlank() && mensaje.isBlank()) {
                return Result.success()
            }

            saveFormularioLocalUseCase(
                FormularioModel(
                    nombre = nombre,
                    mensaje = mensaje
                )
            )
            println("✅ Formulario guardado automáticamente en Room")
            Result.success()
        } catch (e: Exception) {
            println("❌ Error guardando formulario: ${e.message}")
            Result.failure()
        }
    }
}