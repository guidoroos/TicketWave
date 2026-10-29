package nl.guidoroos.ticketwave.ui.utils

import kotlinx.datetime.LocalDateTime
import java.util.Locale

fun formatEventDate(dateTime: LocalDateTime): String {
    val day = String.format(Locale.ROOT, "%02d", dateTime.day)
    val month = String.format(Locale.ROOT, "%02d", dateTime.month.ordinal + 1)
    val year = dateTime.year
    val hour = String.format(Locale.ROOT, "%02d", dateTime.hour)
    val minute = String.format(Locale.ROOT, "%02d", dateTime.minute)
    return "$day/$month/$year - $hour:$minute"
}
