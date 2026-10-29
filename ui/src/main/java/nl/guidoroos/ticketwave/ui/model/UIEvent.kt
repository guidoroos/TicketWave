package nl.guidoroos.ticketwave.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime

@Immutable
data class UIEvent(
    val id: String,
    val title: String,
    val description: String?,
    val startsAt: LocalDateTime?,
    val venue: UIVenue,
    val tickets: List<UITicket>,
    val imageUrl: String
)

@Immutable
data class UIVenue(
    val id: String,
    val name: String,
    val imageUrl: String
)

@Immutable
data class UITicket(
    val id: String,
    val label: String,
    val priceCents: Int,
    val currency: String,
    val quantityAvailable: Int
)
