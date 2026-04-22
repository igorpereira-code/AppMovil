package ucb.edu.bo.formulario.domain.repository

import ucb.edu.bo.formulario.domain.model.FormularioModel

interface FormularioRepository {
    suspend fun saveLocal(model: FormularioModel)
    suspend fun getLatest(): FormularioModel?
    suspend fun syncToFirebase(model: FormularioModel): Result<Unit>
}