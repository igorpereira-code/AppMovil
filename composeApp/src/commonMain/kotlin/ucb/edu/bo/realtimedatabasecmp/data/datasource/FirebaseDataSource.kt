package ucb.edu.bo.realtimedatabasecmp.data.datasource


import ucb.edu.bo.firebase.FirebaseManager

class FirebaseDataSource {
    private val firebaseManager = FirebaseManager()

    suspend fun saveTestValue(path: String, value: String) {
        firebaseManager.saveData(path, value)
    }
}