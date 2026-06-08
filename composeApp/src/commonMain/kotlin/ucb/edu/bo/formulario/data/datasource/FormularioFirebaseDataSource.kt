package ucb.edu.bo.formulario.data.datasource

import ucb.edu.bo.firebase.FirebaseManager
//import ucb.edu.bo.firebase.data.datasource.FirebaseManager

class FormularioFirebaseDataSource {
    private val firebaseManager = FirebaseManager()

    suspend fun syncToFirebase(nombre: String, mensaje: String) {
        firebaseManager.saveData("formulario/nombre", nombre)
        firebaseManager.saveData("formulario/mensaje", mensaje)
    }
}