package ucb.edu.bo.formulario.data.preferences

import android.content.Context
import android.content.SharedPreferences

class FormularioPreferences(context: Context) : IFormularioPreferences {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("formulario_prefs", Context.MODE_PRIVATE)

    override fun saveNombre(nombre: String) {
        prefs.edit().putString("nombre", nombre).apply()
    }

    override fun saveMensaje(mensaje: String) {
        prefs.edit().putString("mensaje", mensaje).apply()
    }

    override fun getNombre(): String = prefs.getString("nombre", "") ?: ""
    override fun getMensaje(): String = prefs.getString("mensaje", "") ?: ""

    override fun clear() {
        prefs.edit().clear().apply()
    }
}