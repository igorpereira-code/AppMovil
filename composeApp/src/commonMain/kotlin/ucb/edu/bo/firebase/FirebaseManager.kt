package ucb.edu.bo.firebase

expect class FirebaseManager() {
    suspend fun saveData(path: String, value: String)
}