package ucb.edu.bo.todoApp.focus_mode.presentation.viewmodel

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

actual fun getCurrentDateString(): String {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

actual fun getDayOfWeekString(): String {
    return when (LocalDate.now().dayOfWeek) {
        DayOfWeek.MONDAY -> "LUN"
        DayOfWeek.TUESDAY -> "MAR"
        DayOfWeek.WEDNESDAY -> "MIÉ"
        DayOfWeek.THURSDAY -> "JUE"
        DayOfWeek.FRIDAY -> "VIE"
        DayOfWeek.SATURDAY -> "SÁB"
        DayOfWeek.SUNDAY -> "DOM"
        else -> ""
    }
}

actual fun generateSessionId(): String {
    return UUID.randomUUID().toString()
}