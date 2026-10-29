package nl.guidoroos.ticketwave.domain.model

data class OwnedTicket(
    val id: String,
    val label: String,
    val priceCents: Int,
    val currency: String,
    val barcode: String,
    val eventId: String? = null,
    val eventTitle: String? = null,
    val eventImageUrl: String? = null,
    val venueName: String? = null,
    val startsAt: String? = null
)
