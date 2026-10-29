package nl.guidoroos.ticketwave.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDateTime

@Immutable
data class UIOwnedTicketGroup(
    val ticketId: String,
    val eventTitle: String,
    val ticketLabel: String,
    val priceCents: Int,
    val currency: String,
    val count: Int,
    val eventImageUrl: String,
    val venueName: String?,
    val startsAt: LocalDateTime?,
    val barcodes: List<String>
) {
    val totalPriceCents: Int
        get() = priceCents * count
}
