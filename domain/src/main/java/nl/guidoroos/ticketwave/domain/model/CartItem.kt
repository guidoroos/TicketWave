package nl.guidoroos.ticketwave.domain.model

data class CartItem(
    val ticketId: String,
    val eventId: String,
    val eventTitle: String,
    val ticketLabel: String,
    val priceCents: Int,
    val currency: String,
    val eventImageUrl: String,
    val event: Event? = null
)
