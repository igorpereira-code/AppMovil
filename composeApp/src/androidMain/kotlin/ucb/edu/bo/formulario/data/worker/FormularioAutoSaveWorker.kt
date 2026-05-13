package ucb.edu.bo.formulario.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ucb.edu.bo.formulario.data.preferences.FormularioPreferences
import ucb.edu.bo.formulario.domain.model.FormularioModel
import ucb.edu.bo.formulario.domain.usecase.SaveFormularioLocalUseCase

class FormularioAutoSaveWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val saveFormularioLocalUseCase: SaveFormularioLocalUseCase by inject()
    private val preferences = FormularioPreferences(appContext)

    override suspend fun doWork(): Result {
        return try {
            val nombre = preferences.getNombre()
            val mensaje = preferences.getMensaje()

            if (nombre.isBlank() && mensaje.isBlank()) {
                println("⚠️ No hay datos en el formulario para guardar")
                return Result.success()
            }

            saveFormularioLocalUseCase(
                FormularioModel(
                    nombre = nombre,
                    mensaje = mensaje
                )
            )
            println("✅ Formulario autoguardado en Room desde SharedPreferences")
            Result.success()
        } catch (e: Exception) {
            println("❌ Error autoguardando: ${e.message}")
            Result.failure()
        }
    }
}