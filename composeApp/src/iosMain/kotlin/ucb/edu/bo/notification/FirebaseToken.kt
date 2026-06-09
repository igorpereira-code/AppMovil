package ucb.edu.bo.firebase

actual suspend fun getToken(): String {
    // Retornamos un string vacío por ahora para iOS hasta que integres Firebase Messaging ahí
    println("getToken() not yet implemented for iOS")
    return ""
}