package ucb.edu.bo.formulario.data.preferences

interface IFormularioPreferences {
    fun saveNombre(nombre: String)
    fun saveMensaje(mensaje: String)
    fun getNombre(): String
    fun getMensaje(): String
    fun clear()
}