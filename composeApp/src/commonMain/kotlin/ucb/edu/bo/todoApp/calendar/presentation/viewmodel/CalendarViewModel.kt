package ucb.edu.bo.todoApp.calendar.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import ucb.edu.bo.todoApp.task.domain.usecase.GetAllTasksUseCase
import ucb.edu.bo.todoApp.calendar.presentation.state.CalendarUIState

class CalendarViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarUIState())
    val state: StateFlow<CalendarUIState> = _state.asStateFlow()

    // Variable interna para saber el inicio de la semana que se está visualizando
    private lateinit var currentWeekStart: LocalDate

    init {
        initializeCalendar()
    }

    private fun initializeCalendar() {
        // 1. Obtener la fecha de hoy según la zona horaria del dispositivo
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        // 2. Calcular el domingo de esta semana
        currentWeekStart = getSundayOfWeek(today)

        // 3. Generar la lista de 7 días y actualizar el estado
        updateCalendarState(selectedDate = today)
        loadTasksForSelectedDate()
    }

    // ── Lógica Matemática del Calendario (KMP) ───────────────────────────────

    private fun getSundayOfWeek(date: LocalDate): LocalDate {
        // En ISO, Monday = 1, Sunday = 7.
        // Si es domingo (7), restamos 0. Si es martes (2), restamos 2 días para llegar a domingo.
        val daysToSubtract = date.dayOfWeek.isoDayNumber % 7
        return date.minus(daysToSubtract, DateTimeUnit.DAY)
    }

    private fun getDaysOfWeek(startDate: LocalDate): List<LocalDate> {
        // A partir del domingo (startDate), sumamos de 0 a 6 días para obtener la semana
        return (0..6).map { startDate.plus(it, DateTimeUnit.DAY) }
    }

    private fun updateCalendarState(selectedDate: LocalDate) {
        val weekDays = getDaysOfWeek(currentWeekStart)

        // El mes y año lo sacamos del día que está seleccionado actualmente
        // .month.name devuelve en inglés (ej. "FEBRUARY"), justo lo que pide tu diseño
        val monthYear = "${selectedDate.month.name}\n${selectedDate.year}"

        _state.value = _state.value.copy(
            selectedDate = selectedDate,
            currentWeekDays = weekDays,
            monthYearText = monthYear
        )
    }

    // ── Formateador de Tiempo (KMP) ──────────────────────────────────────────

    fun formatTaskTimeText(taskDate: kotlinx.datetime.LocalDate?, taskTime: kotlinx.datetime.LocalTime?): String {
        // Si la tarea no tiene fecha u hora, devolvemos un texto por defecto o vacío
        if (taskDate == null || taskTime == null) return "Sin programar"

        // Obtenemos la fecha actual exacta en la zona horaria del dispositivo
        val today = kotlinx.datetime.Clock.System.now()
            .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date

        val tomorrow = today.plus(1, kotlinx.datetime.DateTimeUnit.DAY)

        // Comparamos para saber qué palabra usar
        val dateText = when (taskDate) {
            today -> "Today"
            tomorrow -> "Tomorrow"
            else -> {
                // Si es otro día, formateamos como "May 28"
                // .month.name devuelve todo en mayúsculas (ej. "MAY"), lo capitalizamos
                val monthName = taskDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
                "$monthName ${taskDate.dayOfMonth}"
            }
        }

        // Formateamos la hora asegurando que siempre tenga 2 dígitos (ej. 08:05)
        val hourText = taskTime.hour.toString().padStart(2, '0')
        val minuteText = taskTime.minute.toString().padStart(2, '0')

        return "$dateText At $hourText:$minuteText"
    }

    // ── Acciones del Usuario ──────────────────────────────────────────────────

    fun onDateSelected(date: LocalDate) {
        updateCalendarState(date)
        loadTasksForSelectedDate()
    }

    fun onPreviousWeek() {
        // Retrocedemos 7 días desde el inicio de la semana actual
        currentWeekStart = currentWeekStart.minus(7, DateTimeUnit.DAY)
        // Auto-seleccionamos el primer día de la nueva semana (Domingo)
        onDateSelected(currentWeekStart)
    }

    fun onNextWeek() {
        // Avanzamos 7 días
        currentWeekStart = currentWeekStart.plus(7, DateTimeUnit.DAY)
        onDateSelected(currentWeekStart)
    }

    fun onTabSelected(tab: String) {
        _state.value = _state.value.copy(selectedTab = tab)
        loadTasksForSelectedDate()
    }

    // ── Integración con Casos de Uso ──────────────────────────────────────────

    fun loadTasksForSelectedDate() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val allTasks = getAllTasksUseCase()
            val currentDate = _state.value.selectedDate
            val currentTab = _state.value.selectedTab

            // Filtramos por fecha y por estado (Pendiente vs Completada)
            val filtered = allTasks.filter { task ->
                val matchesDate = task.date == currentDate
                val matchesTab = if (currentTab == "Today") !task.isCompleted else task.isCompleted
                matchesDate && matchesTab
            }

            _state.value = _state.value.copy(
                filteredTasks = filtered,
                isLoading = false
            )
        }
    }
}