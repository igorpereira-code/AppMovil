package ucb.edu.bo.formulario.domain.usecase

import ucb.edu.bo.formulario.domain.model.FormularioModel
import ucb.edu.bo.formulario.domain.repository.FormularioRepository

class SaveFormularioLocalUseCase(
    private val repository: FormularioRepository
) {
    suspend operator fun invoke(model: FormularioModel) {
        repository.saveLocal(model)
    }
}