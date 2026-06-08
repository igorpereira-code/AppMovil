package ucb.edu.bo.formulario.domain.usecase

import ucb.edu.bo.formulario.domain.model.FormularioModel
import ucb.edu.bo.formulario.domain.repository.FormularioRepository

class SyncFormularioUseCase(
    private val repository: FormularioRepository
) {
    suspend operator fun invoke(model: FormularioModel): Result<Unit> {
        return repository.syncToFirebase(model)
    }
}