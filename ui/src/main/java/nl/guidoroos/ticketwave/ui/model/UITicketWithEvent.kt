package nl.guidoroos.ticketwave.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime

@Immutable
data class UITicketWithEvent(
    val eventId: String,
    val eventTitle: String,
    val ticket: UITicket,
    val eventImageUrl: String,
    val venueName: String? = null,
    val startsAt: LocalDateTime? = null
)
