package ucb.edu.bo.formulario.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ucb.edu.bo.formulario.domain.model.FormularioModel
import ucb.edu.bo.formulario.domain.usecase.GetLatestFormularioUseCase
import ucb.edu.bo.formulario.domain.usecase.SaveFormularioLocalUseCase
import ucb.edu.bo.formulario.domain.usecase.SyncFormularioUseCase
import ucb.edu.bo.formulario.presentation.state.FormularioState

class FormularioViewModel(
    private val saveFormularioLocalUseCase: SaveFormularioLocalUseCase,
    private val getLatestFormularioUseCase: GetLatestFormularioUseCase,
    private val syncFormularioUseCase: SyncFormularioUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FormularioState())
    val state: StateFlow<FormularioState> = _state.asStateFlow()

    init {
        loadLatest()
    }

    private fun loadLatest() {
        viewModelScope.launch {
            val latest = getLatestFormularioUseCase()
            if (latest != null) {
                _state.value = _state.value.copy(
                    nombre = latest.nombre,
                    mensaje = latest.mensaje,
                    lastSaved = "Último guardado: ${latest.updatedAt}"
                )
            }
        }
    }

    fun onNombreChange(nombre: String) {
        _state.value = _state.value.copy(nombre = nombre, successMessage = null, errorMessage = null)
    }

    fun onMensajeChange(mensaje: String) {
        _state.value = _state.value.copy(mensaje = mensaje, successMessage = null, errorMessage = null)
    }

    fun saveLocal() {
        viewModelScope.launch {
            try {
                saveFormularioLocalUseCase(
                    FormularioModel(
                        nombre = _state.value.nombre,
                        mensaje = _state.value.mensaje
                    )
                )
                _state.value = _state.value.copy(
                    lastSaved = "✅ Guardado localmente"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error guardando: ${e.message}"
                )
            }
        }
    }

    fun syncToFirebase() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = syncFormularioUseCase(
                FormularioModel(
                    nombre = _state.value.nombre,
                    mensaje = _state.value.mensaje
                )
            )
            _state.value = if (result.isSuccess) {
                _state.value.copy(
                    isLoading = false,
                    isSynced = true,
                    successMessage = "✅ Sincronizado con Firebase"
                )
            } else {
                _state.value.copy(
                    isLoading = false,
                    errorMessage = "❌ Error sincronizando: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }
}