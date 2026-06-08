package ucb.edu.bo.formulario.data.repository

import ucb.edu.bo.formulario.data.dao.FormularioDao
import ucb.edu.bo.formulario.data.datasource.FormularioFirebaseDataSource
import ucb.edu.bo.formulario.data.entity.FormularioEntity
import ucb.edu.bo.formulario.domain.model.FormularioModel
import ucb.edu.bo.formulario.domain.repository.FormularioRepository

class FormularioRepositoryImpl(
    private val dao: FormularioDao,
    private val firebaseDataSource: FormularioFirebaseDataSource
) : FormularioRepository {

    override suspend fun saveLocal(model: FormularioModel) {
        dao.insertOrUpdate(
            FormularioEntity(
                id = model.id,
                nombre = model.nombre,
                mensaje = model.mensaje,
                sincronizado = model.sincronizado,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun getLatest(): FormularioModel? {
        return dao.getLatest()?.let {
            FormularioModel(
                id = it.id,
                nombre = it.nombre,
                mensaje = it.mensaje,
                sincronizado = it.sincronizado,
                updatedAt = it.updatedAt
            )
        }
    }

    override suspend fun syncToFirebase(model: FormularioModel): Result<Unit> {
        return try {
            firebaseDataSource.syncToFirebase(model.nombre, model.mensaje)
            dao.markAsSynced(model.id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}