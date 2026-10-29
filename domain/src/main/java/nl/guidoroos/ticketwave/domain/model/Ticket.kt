package nl.guidoroos.ticketwave.domain.model

data class Ticket(
    val id: String,
    val label: String,
    val priceCents: Int,
    val currency: String,
    val quantityAvailable: Int
)
