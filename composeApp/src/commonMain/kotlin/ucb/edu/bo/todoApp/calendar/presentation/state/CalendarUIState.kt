package ucb.edu.bo.todoApp.calendar.presentation.state

import kotlinx.datetime.LocalDate
import ucb.edu.bo.todoApp.task.domain.model.TaskModel

data class CalendarUIState(
    val selectedDate: LocalDate? = null,
    val currentWeekDays: List<LocalDate> = emptyList(),
    val monthYearText: String = "",
    val selectedTab: String = "Today", // "Today" o "Completed"
    val filteredTasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val isDatePickerVisible: Boolean = false
)