package ucb.edu.bo

// Este objeto vivirá en la memoria de la app y compartirá los datos entre pantallas
object SessionManager {
    var currentUserName: String = "Usuario"
    var currentUserEmail: String = ""

    fun logout() {
        currentUserName = ""
        currentUserEmail = ""
    }
}